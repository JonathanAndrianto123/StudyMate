package com.example.studymate.uiux.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.repository.UserRepository
import com.example.studymate.util.UserPreferences
import kotlinx.coroutines.launch

class SignInViewModel(
    private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onEmailChange(value: String) { email = value }
    fun onPasswordChange(value: String) { password = value }

    fun signIn(onSuccess: () -> Unit) {
        errorMessage = null

        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please fill all fields"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                val user = userRepository.loginUser(email, password)
                if (user != null) {
                    // Save user session
                    val userPrefs = UserPreferences(context)
                    userPrefs.saveCurrentUser(user.id, user.email)
                    
                    isLoading = false
                    onSuccess()
                } else {
                    errorMessage = "Invalid email or password"
                    isLoading = false
                }
            } catch (e: Exception) {
                errorMessage = "Login failed: ${e.message}"
                isLoading = false
            }
        }
    }
}
