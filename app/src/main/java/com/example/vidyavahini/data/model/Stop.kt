package com.example.vidyavahini.data.model

data class Stop(
    val stopIndex: Int = 0,
    val stopName: String = "",
    val landmark: String = "",
    val etaFromPrevious: Int = 0,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)