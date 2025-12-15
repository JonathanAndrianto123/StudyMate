package com.example.studymate.uiux.materi

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.local.entity.MateriEntity
import com.example.studymate.data.repository.MateriRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MateriViewModel(
    private val repository: MateriRepository
) : ViewModel() {

    // ===== LIST DARI ROOM =====
    val materiList = repository.getAllMateri()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList<MateriEntity>()
        )

    // ===== SELECTED =====
    var selectedMateri by mutableStateOf<MateriEntity?>(null)
        private set

//    fun loadMateriById(id: Int) {
//        viewModelScope.launch {
//            selectedMateri = repository.getMateriById(id)
//        }
//    }

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
                    name = name,
                    targetTime = targetTime,
                    description = description
                )
            )
        }
    }

}
