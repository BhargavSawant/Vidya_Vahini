package com.example.vidyavahini.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.vidyavahini.R
import com.example.vidyavahini.data.model.Route
import com.example.vidyavahini.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    selectedRoute: Route?,
    onNavigateBack: () -> Unit,
    onChangeRoute: () -> Unit,
    onStopTracking: () -> Unit,
    onBottomBarNavigate: (String) -> Unit
) {
    if (selectedRoute == null) return

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Configuration.getInstance().userAgentValue = context.packageName
    }

    var snappedRoadPoints by remember(selectedRoute) { mutableStateOf<List<GeoPoint>>(emptyList()) }

    LaunchedEffect(selectedRoute) {
        if (selectedRoute.stops.size > 1) {
            withContext(Dispatchers.IO) {
                try {
                    val roadManager = OSRMRoadManager(context, "VidyaVahiniApp")
                    roadManager.setService("https://router.project-osrm.org/route/v1/driving/")
                    val waypoints = ArrayList<GeoPoint>()
                    selectedRoute.stops.sortedBy { it.stopIndex }.forEach { stop ->
                        waypoints.add(GeoPoint(stop.lat, stop.lng))
                    }
                    val road = roadManager.getRoad(waypoints)
                    if (road.mStatus == org.osmdroid.bonuspack.routing.Road.STATUS_OK) {
                        val roadOverlay = RoadManager.buildRoadOverlay(road)
                        withContext(Dispatchers.Main) {
                            snappedRoadPoints = roadOverlay.points
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            snappedRoadPoints = waypoints
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Live Tracker", fontWeight = FontWeight.Bold, color = NavyBlue) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NavyBlue)
                    }
                },
                actions = {
                    TextButton(onClick = onChangeRoute) {
                        Text("Change Route", color = NavyBlue, fontWeight = FontWeight.Medium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen = "live_map", onNavigate = onBottomBarNavigate)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Current Route", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "${selectedRoute.origin} → Atria",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyBlue
                            )
                        }
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Active",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ETA", fontSize = 12.sp, color = Color.Gray)
                            Text("~49 mins", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Distance", fontSize = 12.sp, color = Color.Gray)
                            Text("14.2 km", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavyBlue)
                        }
                    }
                }
            }

            // Map View - Bigger Map Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { ctx ->
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            setMultiTouchControls(true)
                            controller.setZoom(15.0)
                        }
                    },
                    update = { mapView ->
                        mapView.overlays.clear()
                        if (snappedRoadPoints.isNotEmpty()) {
                            val polyline = Polyline(mapView).apply {
                                setPoints(snappedRoadPoints)
                                outlinePaint.color = android.graphics.Color.parseColor("#006B31")
                                outlinePaint.strokeWidth = 10f
                            }
                            mapView.overlays.add(polyline)
                            val boundingBox = org.osmdroid.util.BoundingBox.fromGeoPoints(snappedRoadPoints)
                            mapView.zoomToBoundingBox(boundingBox, true, 80)
                        }
                        selectedRoute.stops.forEach { stop ->
                            val stopMarker = Marker(mapView).apply {
                                position = GeoPoint(stop.lat, stop.lng)
                                icon = ContextCompat.getDrawable(context, R.drawable.ic_stop_node)
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                            }
                            mapView.overlays.add(stopMarker)
                        }
                        mapView.invalidate()
                    }
                )
            }

            // Upcoming Stops List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Upcoming Stops", fontWeight = FontWeight.Bold, color = NavyBlue, fontSize = 16.sp)
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = null, tint = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        itemsIndexed(selectedRoute.stops) { index, stop ->
                            StopItem(
                                stopName = stop.stopName,
                                timeInfo = if (index == 0) "Passed at 08:15 AM" else if (index == 1) "ETA: 5 mins away" else "Scheduled: 08:30 AM",
                                isPassed = index == 0,
                                isNext = index == 1,
                                isLast = index == selectedRoute.stops.size - 1
                            )
                        }
                    }

                    // Stop Tracking Button
                    Button(
                        onClick = onStopTracking,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF1F1)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Stop Live Tracker", color = EmergencyRed, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StopItem(stopName: String, timeInfo: String, isPassed: Boolean, isNext: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(if (isPassed) SuccessGreen else if (isNext) NavyBlue else Color.LightGray, shape = RoundedCornerShape(50))
            )
            if (!isLast) {
                Box(modifier = Modifier.width(2.dp).height(40.dp).background(Color.LightGray))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = stopName, fontWeight = if (isNext) FontWeight.Bold else FontWeight.Medium, color = if (isNext) NavyBlue else Color.DarkGray)
            Text(text = timeInfo, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
