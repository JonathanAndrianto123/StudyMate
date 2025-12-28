package com.example.studymate.uiux.materi

data class MateriUiState(
    val id: Int,
    val name: String,
    val targetTime: String,
    val progressTime: String,
    val progress: Int,
    val todayTime: String,
    val totalTime: String
)
