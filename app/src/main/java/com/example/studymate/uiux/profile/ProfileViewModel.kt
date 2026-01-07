package com.example.studymate.uiux.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.repository.UserRepository
import com.example.studymate.util.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class ProfileViewModel(
    private val context: Context,
    private val userRepository: UserRepository,
    private val userId: Int
) : ViewModel() {

    val user = userRepository.getUserById(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logout(onLogout: () -> Unit) {
        val prefs = UserPreferences(context)
        prefs.clearSession()
        onLogout()
    }
}
