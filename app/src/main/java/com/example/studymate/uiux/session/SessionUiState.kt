package com.example.studymate.uiux.session

data class SessionUiState(
    val materiName: String,
    val duration: String,
    val date: String,
    val distractions: Int = 0,
    val location: String = "Unknown Location"
)
