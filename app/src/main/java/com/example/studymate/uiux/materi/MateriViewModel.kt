package com.example.studymate.uiux.materi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MateriViewModel : ViewModel() {

    // ================= LIST =================
    var materiList = mutableStateOf(
        listOf(
            MateriUiState(
                id = 1,
                name = "Matematika Teknik",
                targetTime = "02 : 15 : 00",
                progressTime = "0%",
                progress = 40,
                todayTime = "0 hr 00 min",
                totalTime = "1 hr 20 min"
            ),
            MateriUiState(
                id = 2,
                name = "Algoritma Graph",
                targetTime = "03 : 45 : 00",
                progressTime = "50%",
                progress = 50,
                todayTime = "0 hr 30 min",
                totalTime = "2 hr 10 min"
            )
        )
    )
        private set

    // ================= SELECTED =================
    var selectedMateri by mutableStateOf<MateriUiState?>(null)
        private set

    // ================= ACTION =================
    fun selectMateri(materi: MateriUiState) {
        selectedMateri = materi
    }
}
