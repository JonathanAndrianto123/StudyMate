package com.example.studymate.uiux.session

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studymate.data.repository.StudySessionRepository
import com.example.studymate.util.UserPreferences
import com.example.studymate.sensor.FlipDetector
import com.example.studymate.location.LocationProvider

class SessionViewModelFactory(
    private val context: Context,
    private val studySessionRepository: StudySessionRepository,
    private val userId: Int,
    private val flipDetector: FlipDetector? = null,
    private val locationProvider: LocationProvider? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel(context, studySessionRepository, userId, flipDetector, locationProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
