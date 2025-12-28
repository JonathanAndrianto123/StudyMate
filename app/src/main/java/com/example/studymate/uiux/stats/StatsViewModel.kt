package com.example.studymate.uiux.stats

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.repository.MateriRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    private val repository: MateriRepository,
    private val sessionRepository: com.example.studymate.data.repository.StudySessionRepository,
    private val userId: Int
) : ViewModel() {

    val stats = repository.getAllMateri(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
        
    val totalDistractions = sessionRepository.getAllSessions()
        .map { sessions -> 
             sessions.filter { it.userId == userId }.sumOf { it.distractionCount }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )
}
