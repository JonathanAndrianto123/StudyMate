package com.example.studymate.uiux.profile

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.repository.UserRepository
import com.example.studymate.util.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var isLoading by mutableStateOf(true)

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                val userPrefs = UserPreferences(context)
                val userId = userPrefs.getCurrentUserId()
                
                if (userId != -1) {
                    val userEntity = userRepository.getUserById(userId).first()
                    userEntity?.let {
                        name = it.name
                        email = it.email
                    }
                } else {
                    // No user logged in
                    name = "Guest"
                    email = "Not logged in"
                }
                isLoading = false
            } catch (e: Exception) {
                name = "Error"
                email = "Failed to load"
                isLoading = false
            }
        }
    }

    fun triggerDemoReminder() {
        val workRequest = androidx.work.OneTimeWorkRequestBuilder<com.example.studymate.worker.StudyReminderWorker>()
            .setInitialDelay(5, java.util.concurrent.TimeUnit.SECONDS)
            .build()
        
        androidx.work.WorkManager.getInstance(context).enqueue(workRequest)
    }
}
