package com.example.studymate.uiux.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.repository.MateriRepository
import com.example.studymate.data.repository.StudySessionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    private val materiRepository: MateriRepository,
    private val sessionRepository: StudySessionRepository,
    private val userId: Int
) : ViewModel() {

    val materiList = materiRepository.getAllMateri(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Future implementation for stats logic
}
