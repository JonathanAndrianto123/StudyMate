package com.example.studymate.uiux.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    var name by mutableStateOf("Mingyu")
    var email by mutableStateOf("mingyu@gmail.com")
}
