package com.example.vidyavahini.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidyavahini.data.local.RoutePreferenceManager
import com.example.vidyavahini.data.model.Route
import com.example.vidyavahini.data.repository.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RouteViewModel(
    application: Application,
    private val repository: RouteRepository = RouteRepository()
) : AndroidViewModel(application) {

    private val prefsManager = RoutePreferenceManager(application)

    private val _availableRoutes = MutableStateFlow<List<Route>>(emptyList())
    val availableRoutes: StateFlow<List<Route>> = _availableRoutes.asStateFlow()

    private val _selectedRoute = MutableStateFlow<Route?>(null)
    val selectedRoute: StateFlow<Route?> = _selectedRoute.asStateFlow()

    init {
        fetchRoutes()
    }

    private fun fetchRoutes() {
        viewModelScope.launch {
            repository.getAvailableRoutes().collect { routes ->
                _availableRoutes.value = routes

                val savedId = prefsManager.getSavedRouteId()
                if (savedId != null && savedId.isNotEmpty()) {
                    val foundRoute = routes.find { it.routeId == savedId }
                    if (foundRoute != null) {
                        _selectedRoute.value = foundRoute
                    }
                }
            }
        }
    }

    fun selectRoute(route: Route) {
        _selectedRoute.value = route
        prefsManager.saveRouteId(route.routeId)
    }

    fun clearSelectedRoute() {
        _selectedRoute.value = null
        prefsManager.saveRouteId("")
    }

    fun reportBreakdown(routeId: String) {
        viewModelScope.launch {
            repository.reportBreakdown(routeId, true)
        }
    }
}
