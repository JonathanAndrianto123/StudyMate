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
import org.mindrot.jbcrypt.BCrypt

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

    fun onNameChange(v: String) { name = v }
    fun onEmailChange(v: String) { email = v }
    fun onPasswordChange(v: String) { password = v }
    fun onConfirmPasswordChange(v: String) { confirmPassword = v }

    fun signUp(onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            errorMessage = "Please fill all fields"
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                // Check if user exists
                val existing = userRepository.getUserByEmail(email)
                if (existing != null) {
                    errorMessage = "Email already registered"
                    isLoading = false
                    return@launch
                }

                // Hash password
                val hashed = BCrypt.hashpw(password, BCrypt.gensalt())
                
                val user = UserEntity(
                    name = name,
                    email = email,
                    password = hashed
                )
                
                val id = userRepository.registerUser(user)
                
                // Auto login after sign up?
                if (id > 0) {
                     val prefs = UserPreferences(context)
                     prefs.saveCurrentUser(id.toInt(), email)
                     onSuccess()
                } else {
                    errorMessage = "Registration failed"
                }

            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }
}
