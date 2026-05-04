package com.example.vidyavahini.data.model

data class Route(
    val routeId: String = "",
    val name: String = "",
    val origin: String = "",
    val destination: String = "",
    val avgInterStopMins: Int = 0,
    val breakdownStatus: Boolean = false,
    val stops: List<Stop> = emptyList(),
    val liveLat: Double? = null,
    val liveLng: Double? = null
)