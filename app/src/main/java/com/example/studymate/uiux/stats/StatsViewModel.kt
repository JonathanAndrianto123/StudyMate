package com.example.studymate.uiux.stats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// UI model khusus untuk statistik
data class StatsUiState(
    val materiName: String,
    val totalTime: String,
    val progress: Int
)

class StatsViewModel : ViewModel() {

    // ===== STATE =====
    var stats by mutableStateOf(listOf<StatsUiState>())
        private set

    init {
        loadStats()
    }

    // ===== LOGIC =====
    private fun loadStats() {
        // sementara dummy data
        // nanti gampang diganti dari Repository / Room
        stats = listOf(
            StatsUiState(
                materiName = "Matematika Teknik",
                totalTime = "1h 20m",
                progress = 40
            ),
            StatsUiState(
                materiName = "Algoritma Graph",
                totalTime = "2h 10m",
                progress = 60
            )
        )
    }
}
