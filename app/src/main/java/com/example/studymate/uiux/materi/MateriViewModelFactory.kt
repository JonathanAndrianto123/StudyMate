package com.example.studymate.uiux.materi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studymate.data.repository.MateriRepository

class MateriViewModelFactory(
    private val repository: MateriRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MateriViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MateriViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
