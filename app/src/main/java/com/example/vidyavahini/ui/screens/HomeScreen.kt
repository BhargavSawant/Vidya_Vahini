package com.example.vidyavahini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vidyavahini.data.model.Route
import com.example.vidyavahini.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    selectedRoute: Route?,
    onNavigateToRoute: () -> Unit,
    onNavigateToPing: () -> Unit,
    onNavigateToBreakdown: () -> Unit,
    onNavigateToSelectRoute: () -> Unit,
    onNavigateToDriverMode: () -> Unit,
    onBottomBarNavigate: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Vidya-Vahini", color = NavyBlue, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Open drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = NavyBlue)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                         Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.fillMaxSize())
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "home", onNavigate = onBottomBarNavigate)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome Student!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
            Text(
                text = "Here is your quick access dashboard for today's travel.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            DashboardCard(
                title = "Select My Route",
                description = "Choose your bus route for today.",
                icon = Icons.AutoMirrored.Filled.AltRoute,
                onClick = onNavigateToSelectRoute
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardCard(
                title = "View Live Tracker",
                description = if (selectedRoute != null) "Your route is active. Track now." else "Select a route to track.",
                icon = Icons.Default.Map,
                isActive = selectedRoute != null,
                badge = if (selectedRoute != null) "ACTIVE" else null,
                onClick = onNavigateToRoute
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardCard(
                title = "Ping Bus Location",
                description = "Share current bus location updates.",
                icon = Icons.Default.AddLocationAlt,
                onClick = onNavigateToPing
            )

            Spacer(modifier = Modifier.height(16.dp))

            DashboardCard(
                title = "Report Bus Breakdown",
                description = "Immediately notify administration of any vehicle issues or delays.",
                icon = Icons.Default.Warning,
                badge = "Emergency",
                badgeColor = Color(0xFFFDE7E7),
                badgeTextColor = EmergencyRed,
                onClick = onNavigateToBreakdown
            )

            Spacer(modifier = Modifier.weight(1f))

            // Driver Mode Prototype Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToDriverMode() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = ActionBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Switch to Driver Mode", fontWeight = FontWeight.Bold, color = NavyBlue)
                        Text("Prototype: Broadcast GPS location", fontSize = 12.sp, color = ActionBlue)
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    description: String,
    icon: ImageVector,
    isActive: Boolean = false,
    badge: String? = null,
    badgeColor: Color = Color(0xFFE8F5E9),
    badgeTextColor: Color = Color(0xFF2E7D32),
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) NavyBlue else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color(0x33FFFFFF) else Color(0xFFF0F4F8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (isActive) Color.White else NavyBlue,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (isActive) Color.White else NavyBlue
                    )
                    badge?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = if (isActive) Color(0xFF2E7D32) else badgeColor,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) Color.White else badgeTextColor
                            )
                        }
                    }
                }
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = if (isActive) Color(0xFFB0BEC5) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentScreen: String, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = currentScreen == "home",
            onClick = { onNavigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentScreen == "routes",
            onClick = { onNavigate("routes") },
            icon = { Icon(Icons.Default.Route, contentDescription = null) },
            label = { Text("Routes") }
        )
        NavigationBarItem(
            selected = currentScreen == "live_map",
            onClick = { onNavigate("live_map") },
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            label = { Text("Live Map") }
        )
        NavigationBarItem(
            selected = currentScreen == "alert",
            onClick = { onNavigate("alert") },
            icon = { Icon(Icons.Default.Notifications, contentDescription = null) },
            label = { Text("Alert") }
        )
    }
}
