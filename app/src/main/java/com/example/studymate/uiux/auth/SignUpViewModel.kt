package com.example.studymate.uiux.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.local.entity.UserEntity
import com.example.studymate.data.repository.UserRepository
import com.example.studymate.util.UserPreferences
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val context: Context,
    private val userRepository: UserRepository
) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun onNameChange(value: String) { name = value }
    fun onEmailChange(value: String) { email = value }
    fun onPasswordChange(value: String) { password = value }
    fun onConfirmPasswordChange(value: String) { confirmPassword = value }

    fun signUp(onSuccess: () -> Unit) {
        errorMessage = null

        // Validation
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Please fill all fields"
            return
        }

        if (password != confirmPassword) {
            errorMessage = "Passwords don't match"
            return
        }

        if (password.length < 6) {
            errorMessage = "Password must be at least 6 characters"
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                // Check if email already exists
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    errorMessage = "Email already registered"
                    isLoading = false
                    return@launch
                }

                // Create new user
                val hashed = org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt())
                val user = UserEntity(
                    name = name,
                    email = email,
                    password = hashed
                )

                val userId = userRepository.registerUser(user)
                
                // Save user session
                val userPrefs = UserPreferences(context)
                userPrefs.saveCurrentUser(userId.toInt(), email)
                
                isLoading = false
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Registration failed: ${e.message}"
                isLoading = false
            }
        }
    }
}
