package com.example.vidyavahini.data.repository

import com.example.vidyavahini.data.model.Ping
import com.example.vidyavahini.data.model.Route
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RouteRepository {

    // Connect to your live database
    private val database = FirebaseDatabase.getInstance("https://vidya-vahini-03-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val routesRef = database.getReference("routes")
    private val pingsRef = database.getReference("pings")

    // Fetch the live list from the cloud
    fun getAvailableRoutes(): Flow<List<Route>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val routesList = mutableListOf<Route>()
                for (childSnapshot in snapshot.children) {
                    val route = childSnapshot.getValue(Route::class.java)
                    if (route != null) {
                        routesList.add(route)
                    }
                }
                trySend(routesList)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        routesRef.addValueEventListener(listener)
        awaitClose { routesRef.removeEventListener(listener) }
    }

    // Push the live ping to the cloud (FR-VV-05)
    fun submitPing(routeId: String, ping: Ping) {
        pingsRef.child(routeId).child("latest").setValue(ping)
    }

    // Update breakdown status in the database (Requirement 4)
    fun reportBreakdown(routeId: String, isBroken: Boolean) {
        routesRef.child(routeId).child("breakdownStatus").setValue(isBroken)
    }
}
