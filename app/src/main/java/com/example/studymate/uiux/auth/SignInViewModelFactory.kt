package com.example.studymate.uiux.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studymate.data.repository.UserRepository

class SignInViewModelFactory(
    private val context: Context,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(context, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
