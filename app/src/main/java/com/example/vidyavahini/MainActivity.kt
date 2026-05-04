package com.example.vidyavahini

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.example.vidyavahini.ui.screens.*
import com.example.vidyavahini.ui.theme.VidyaVahiniTheme
import com.example.vidyavahini.ui.viewmodels.RouteViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            VidyaVahiniTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    VidyaVahiniApp(application)
                }
            }
        }
    }
}

@Composable
fun VidyaVahiniApp(application: Application) {
    val navController = rememberNavController()

    val routeViewModel: RouteViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                RouteViewModel(application)
            }
        }
    )

    val selectedRoute by routeViewModel.selectedRoute.collectAsState()

    // Extracting bottom navigation logic to be shared between screens
    val onBottomBarNavigate: (String) -> Unit = { screen ->
        when (screen) {
            "home" -> navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
            "routes" -> navController.navigate("select_route")
            "live_map" -> {
                if (selectedRoute != null) {
                    navController.navigate("route_details")
                } else {
                    navController.navigate("select_route")
                }
            }
            "alert" -> navController.navigate("breakdown_screen")
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                selectedRoute = selectedRoute,
                onNavigateToRoute = { navController.navigate("route_details") },
                onNavigateToPing = { navController.navigate("ping_screen") },
                onNavigateToBreakdown = { navController.navigate("breakdown_screen") },
                onNavigateToSelectRoute = { navController.navigate("select_route") },
                onNavigateToDriverMode = { navController.navigate("driver_mode") },
                onBottomBarNavigate = onBottomBarNavigate
            )
        }

        composable("select_route") {
            RouteSelectionScreen(
                viewModel = routeViewModel,
                onRouteSelected = {
                    navController.navigate("route_details") {
                        popUpTo("home")
                    }
                },
                onBottomBarNavigate = onBottomBarNavigate
            )
        }

        composable("route_details") {
            RouteScreen(
                selectedRoute = selectedRoute,
                onNavigateBack = { navController.popBackStack() },
                onChangeRoute = {
                    navController.navigate("select_route")
                },
                onStopTracking = {
                    routeViewModel.clearSelectedRoute()
                    navController.popBackStack("home", inclusive = false)
                },
                onBottomBarNavigate = onBottomBarNavigate
            )
        }

        composable("ping_screen") {
            PingScreen(
                selectedRoute = selectedRoute,
                onPingSubmitted = {
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() },
                onBottomBarNavigate = onBottomBarNavigate
            )
        }

        composable("breakdown_screen") {
            BreakdownScreen(
                selectedRoute = selectedRoute,
                onReportConfirmed = {
                    selectedRoute?.let { routeViewModel.reportBreakdown(it.routeId) }
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() },
                onBottomBarNavigate = onBottomBarNavigate
            )
        }

        composable("driver_mode") {
            DriverModeScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
