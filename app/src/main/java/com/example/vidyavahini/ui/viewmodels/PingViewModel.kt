package com.example.vidyavahini.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vidyavahini.data.model.Ping
import com.example.vidyavahini.data.repository.RouteRepository

class PingViewModel(
    private val repository: RouteRepository = RouteRepository()
) : ViewModel() {

    /**
     * Called when the student taps the "PING" button.
     * Satisfies FR-VV-04 and FR-VV-05.
     */
    fun submitBusPing(routeId: String, landmarkIndex: Int, studentStop: String) {
        val newPing = Ping(
            // Firebase handles the unique push ID internally if needed, but we are writing
            // to the "latest" node anyway to prevent unbounded growth.
            routeId = routeId,
            landmarkIndex = landmarkIndex,
            submittedByStop = studentStop,
            timestamp = System.currentTimeMillis() // Epoch ms required by SRD
        )

        repository.submitPing(routeId, newPing)
    }
}