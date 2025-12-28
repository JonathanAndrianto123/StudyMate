package com.example.studymate.uiux.materi

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.local.entity.MateriEntity
import com.example.studymate.data.repository.MateriRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MateriViewModel(
    private val repository: MateriRepository,
    private val userId: Int
) : ViewModel() {

    // ===== LIST DARI ROOM SCOPED BY USER =====
    val materiList = repository.getAllMateri(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList<MateriEntity>()
        )

    var selectedMateri by mutableStateOf<MateriEntity?>(null)
        private set

    fun getMateriById(id: Int): Flow<MateriEntity?> {
        return repository.getMateriById(id)
    }

    fun addMateri(
        name: String,
        targetTime: String,
        description: String
    ) {
        viewModelScope.launch {
            repository.addMateri(
                MateriEntity(
                    userId = userId,
                    name = name,
                    targetTime = targetTime,
                    description = description
                )
            )
        }
    }
}
