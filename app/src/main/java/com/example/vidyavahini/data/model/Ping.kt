package com.example.vidyavahini.data.model

data class Ping(
    val pingId: String = "",
    val routeId: String = "",
    val landmarkIndex: Int = 0,
    val submittedByStop: String = "",
    val timestamp: Long = 0L
)