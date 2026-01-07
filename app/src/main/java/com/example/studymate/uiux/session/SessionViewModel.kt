package com.example.studymate.uiux.session

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.data.local.entity.StudySessionEntity
import com.example.studymate.data.repository.StudySessionRepository
import com.example.studymate.location.LocationProvider
import com.example.studymate.location.PlaceResolver
import com.example.studymate.sensor.FlipDetector
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionViewModel(
    private val context: Context,
    private val studySessionRepository: StudySessionRepository,
    private val userId: Int,
    private val flipDetector: FlipDetector? = null,
    private val locationProvider: LocationProvider? = null
) : ViewModel() {

    // Timer State
    var isRunning by mutableStateOf(false)
    var elapsedSeconds by mutableStateOf(0L)
    var formattedTime by mutableStateOf("00:00")
    
    // Target / Pomodoro (optional usage based on stub)
    var remainingSeconds by mutableStateOf(0L)
    var formattedRemainingTime by mutableStateOf("00:00")
    var targetReached by mutableStateOf(false)
    private var targetSecondsTotal = 0L

    // Session Data
    var materiId by mutableStateOf(0)
    var materiName by mutableStateOf("")
    var distractionCount by mutableStateOf(0)
    var isGracePeriod by mutableStateOf(false)
    
    // History
    var sessionList by mutableStateOf<List<SessionUiState>>(emptyList())

    // Internals
    private var timerJob: Job? = null
    private var startTimestamp = 0L
    private var previousTotalSeconds = 0L
    
    // Location
    private var startLatitude: Double? = null
    private var startLongitude: Double? = null

    init {
        observeFlipDetector()
        flipDetector?.start()
    }

    fun setMateri(id: Int, name: String, targetTime: String) {
        materiId = id
        materiName = name
        targetSecondsTotal = parseTargetTime(targetTime).toLong() * 60 // assuming minutes input
        remainingSeconds = targetSecondsTotal
        updateRemainingTime()
    }
    
    // Helper to parse target time string to minutes
    private fun parseTargetTime(targetTime: String): Double {
        return targetTime.toDoubleOrNull() ?: 0.0
    }

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        startTimestamp = System.currentTimeMillis()
        
        // Fetch location on start
        locationProvider?.fetchOneTimeLocation(
            onSuccess = { lat, lon ->
                startLatitude = lat
                startLongitude = lon
            },
            onError = { /* optimize: handle error or ignore */ }
        )

        // Start Grace Period
        viewModelScope.launch {
            isGracePeriod = true
            delay(3000) // 3 seconds grace period
            isGracePeriod = false
        }

        timerJob = viewModelScope.launch {
            while (isRunning) {
                val currentTimestamp = System.currentTimeMillis()
                val sessionDuration = (currentTimestamp - startTimestamp) / 1000
                elapsedSeconds = previousTotalSeconds + sessionDuration
                
                // Update formatted time
                formattedTime = formatSeconds(elapsedSeconds)
                
                // Update remaining
                if (targetSecondsTotal > 0) {
                    val left = targetSecondsTotal - elapsedSeconds
                    remainingSeconds = if (left > 0) left else 0
                    updateRemainingTime()
                    
                    if (remainingSeconds == 0L && !targetReached) {
                        targetReached = true
                        sendTargetReachedNotification()
                    }
                }
                
                delay(1000)
            }
        }
    }

    fun pauseTimer() {
        if (!isRunning) return
        isRunning = false
        timerJob?.cancel()
        val currentTimestamp = System.currentTimeMillis()
        previousTotalSeconds += (currentTimestamp - startTimestamp) / 1000
    }
    
    // Implement stubs for auto-pause/resume if needed
    fun autoPauseTimer() {
        if (isRunning) pauseTimer()
    }
    
    fun autoResumeTimer() {
        if (!isRunning) startTimer()
    }

    fun stopTimer(onComplete: () -> Unit) {
        pauseTimer() // Consolidate duration
        
        // Save Session
        viewModelScope.launch {
            val session = StudySessionEntity(
                userId = userId,
                materiId = materiId,
                materiName = materiName,
                startTime = System.currentTimeMillis() - (elapsedSeconds * 1000), // Approx start
                endTime = System.currentTimeMillis(),
                durationMs = elapsedSeconds * 1000,
                latitude = startLatitude,
                longitude = startLongitude,
                distractionCount = distractionCount
            )
            studySessionRepository.saveSession(session)
            
            // Generate simple notification or vibration
            vibratePhone() 
            onComplete()
            
            // Reset state
            elapsedSeconds = 0
            previousTotalSeconds = 0
            formattedTime = "00:00"
            distractionCount = 0
            targetReached = false
        }
    }

    private fun observeFlipDetector() {
        viewModelScope.launch {
            // We want to detect if phone is face UP during session (distraction)
            // FlipDetector.isFaceDown is true when face down.
            // Distraction = isRunning && !isGracePeriod && !isFaceDown
            
            var wasFaceDown = false
            
            flipDetector?.isFaceDown?.collect { isFaceDown ->
                if (isRunning && !isGracePeriod) {
                    if (wasFaceDown && !isFaceDown) {
                        // Value changed from Down to Up -> Distraction
                        handleDistraction()
                    }
                }
                wasFaceDown = isFaceDown
            }
        }
    }

    private fun handleDistraction() {
        distractionCount++
        sendDistractionNotification()
        vibratePhone()
    }

    fun loadSessionsForMateri(materiId: Int) {
        viewModelScope.launch {
            studySessionRepository.getSessionsByMateri(materiId).collect { sessions ->
                // Map to UI
                val resolver = PlaceResolver(context)
                mapSessionsToUi(sessions, resolver)
            }
        }
    }
    
    private suspend fun mapSessionsToUi(sessions: List<StudySessionEntity>, placeResolver: PlaceResolver) {
        val uiList = sessions.map { session ->
            val locName = if (session.latitude != null && session.longitude != null) {
                placeResolver.resolvePlaceName(session.latitude, session.longitude)
            } else {
                "Unknown"
            }
            
            SessionUiState(
                materiName = session.materiName,
                duration = formatSeconds(session.durationMs / 1000),
                date = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(session.startTime)),
                distractions = session.distractionCount,
                location = locName
            )
        }
        sessionList = uiList
    }

    private fun formatSeconds(seconds: Long): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    private fun updateRemainingTime() {
        formattedRemainingTime = formatSeconds(remainingSeconds)
    }

    private fun sendTargetReachedNotification() {
        // Placeholder
    }

    private fun sendDistractionNotification() {
        // Placeholder
    }
    
    private fun vibratePhone() {
        // Placeholder
    }
    
    private fun createNotificationChannel() {
        // Placeholder
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        flipDetector?.stop()
    }
}
