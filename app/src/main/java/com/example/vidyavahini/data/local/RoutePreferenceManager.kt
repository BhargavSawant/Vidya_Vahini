package com.example.vidyavahini.data.local

import android.content.Context
import android.content.SharedPreferences

class RoutePreferenceManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("vidya_vahini_prefs", Context.MODE_PRIVATE)

    fun saveRouteId(routeId: String) {
        prefs.edit().putString("SAVED_ROUTE_ID", routeId).apply()
    }

    fun getSavedRouteId(): String? {
        return prefs.getString("SAVED_ROUTE_ID", null)
    }
}