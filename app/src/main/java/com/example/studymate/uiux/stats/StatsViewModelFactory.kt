package com.example.studymate.uiux.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studymate.data.repository.MateriRepository

class StatsViewModelFactory(
    private val repository: MateriRepository,
    private val sessionRepository: com.example.studymate.data.repository.StudySessionRepository,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatsViewModel(repository, sessionRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
