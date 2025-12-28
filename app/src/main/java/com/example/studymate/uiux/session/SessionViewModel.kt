package com.example.studymate.uiux.session

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studymate.R
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.local.entity.StudySessionEntity
import com.example.studymate.data.repository.MateriRepository
import com.example.studymate.data.repository.StudySessionRepository
import com.example.studymate.location.LocationProvider
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

    var isRunning by mutableStateOf(false)
    var elapsedSeconds by mutableStateOf(0L)
    var formattedTime by mutableStateOf("00:00:00")
    
    // Advanced Info
    var remainingSeconds by mutableStateOf(0L)
    var formattedRemainingTime by mutableStateOf("00:00:00")
    var targetReached by mutableStateOf(false)
    private var targetSecondsTotal = 0L
    private var previousTotalSeconds = 0L

    // Untuk tracking materi mana yg lagi dipelajari
    var materiId by mutableStateOf(0)
    var materiName by mutableStateOf("")

    private var timerJob: Job? = null
    private var startTimestamp: Long = 0
    

    // Location data
    private var startLatitude: Double? = null
    private var startLongitude: Double? = null
    
    // Distraction tracking
    var distractionCount by mutableStateOf(0)
    
    // Grace period state
    var isGracePeriod by mutableStateOf(false)

    var sessionList by mutableStateOf<List<SessionUiState>>(emptyList())

    init {
        createNotificationChannel()
        observeFlipDetector()
        loadSessions() // Load history on start
    }

    private fun loadSessions() {
        val placeResolver = com.example.studymate.location.PlaceResolver(context)
        
        viewModelScope.launch {
            studySessionRepository.getAllSessions().collect { sessions ->
                // Default: load all for user
                val userSessions = sessions.filter { it.userId == userId }
                mapSessionsToUi(userSessions, placeResolver)
            }
        }
    }

    fun loadSessionsForMateri(materiId: Int) {
        val placeResolver = com.example.studymate.location.PlaceResolver(context)
        
        viewModelScope.launch {
            studySessionRepository.getSessionsByMateri(materiId).collect { sessions ->
                 // already filtered by materiId in DAO, just check userId
                 val userSessions = sessions.filter { it.userId == userId }
                 mapSessionsToUi(userSessions, placeResolver)
            }
        }
    }

    private fun mapSessionsToUi(sessions: List<StudySessionEntity>, placeResolver: com.example.studymate.location.PlaceResolver) {
        sessionList = sessions.map { session ->
            val locationStr = if (session.latitude != null && session.longitude != null) {
                placeResolver.resolve(session.latitude, session.longitude)
            } else {
                "Unknown Location"
            }

            SessionUiState(
                materiName = session.materiName,
                duration = formatSeconds(session.durationMs / 1000),
                date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(session.startTime)),
                distractions = session.distractionCount,
                location = locationStr
            )
        }
    }

    private fun observeFlipDetector() {
        flipDetector?.let { detector ->
            viewModelScope.launch {
                detector.isFaceDown.collect { isFaceDown ->
                    // Logic: If Face Up (user looking at phone) -> Pause & Distract
                    // BUT ignore if in grace period
                    if (!isFaceDown && isRunning && !isGracePeriod) {
                        handleDistraction()
                    } else if (isFaceDown && !isRunning && elapsedSeconds > 0) {
                         // Auto resume if they put it back down
                        autoResumeTimer()
                    }
                }
            }
        }
    }
    
    private fun handleDistraction() {
        autoPauseTimer()
        distractionCount++ 
        vibratePhone()
        sendDistractionNotification()
    }
    
    private fun vibratePhone() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as android.os.VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(android.os.VibrationEffect.createOneShot(500, android.os.VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
            vibrator.vibrate(500)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Study Timer"
            val descriptionText = "Notifications for study timer updates"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("study_timer_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateRemainingTime() {
        if (targetSecondsTotal > 0) {
            val totalSecondsInternal = previousTotalSeconds + elapsedSeconds
            
            remainingSeconds = if (totalSecondsInternal < targetSecondsTotal) {
                targetSecondsTotal - totalSecondsInternal
            } else {
                0
            }
            formattedRemainingTime = formatSeconds(remainingSeconds)

            if (remainingSeconds == 0L && !targetReached) {
                targetReached = true
                sendTargetReachedNotification()
            }
        }
    }

    private fun sendTargetReachedNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "study_timer_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Study Target Reached!")
            .setContentText("You have completed your study goal for $materiName.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(2, notification)
    }

    fun setMateri(id: Int, name: String, targetTime: String) {
        materiId = id
        materiName = name
        
        // Calculate target seconds
        val targetHours = parseTargetTime(targetTime)
        targetSecondsTotal = (targetHours * 3600).toLong()
        
        viewModelScope.launch {
            val totalDurationMs = studySessionRepository.getTotalDurationForMateri(materiId)
            previousTotalSeconds = totalDurationMs / 1000
            
            // Allow override if target finished, but usually we just show 0 remaining.
            
            // Reset state for new materi
            elapsedSeconds = 0
            
            // Calculate initial remaining
            remainingSeconds = if (previousTotalSeconds < targetSecondsTotal) {
                targetSecondsTotal - previousTotalSeconds
            } else {
                0
            }
            
            formattedTime = formatSeconds(0)
            formattedRemainingTime = formatSeconds(remainingSeconds)
            
            // Check if already reached
            targetReached = remainingSeconds == 0L
            distractionCount = 0
        }
    }

    fun pauseTimer() {
        isRunning = false
        timerJob?.cancel()
    }
    
    fun autoPauseTimer() {
        if (isRunning) {
            pauseTimer()
        }
    }

    fun autoResumeTimer() {
        if (!isRunning) {
            startTimer()
        }
    }

    private fun sendDistractionNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "study_timer_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("Distraction Detected!")
            .setContentText("Please focus on your study. Distractions so far: $distractionCount")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(3, notification)
    }
    fun startTimer() {
        if (isRunning) return
        isRunning = true
        startTimestamp = System.currentTimeMillis()
        
        // Immediate flip check requested by user
        // If we are NOT face down (meaning we are looking at screen), we consider it distracted/paused?
        // Or maybe we give a grace period. User said "langsung mengecek".
        // Use a small delay to allow putting phone down? No, user said "langsung".
        // But if we pause immediately, we can't start.
        // Let's assume start implies intent. We'll let the observer handle it.
        // The observer runs in a coroutine.
        
        // However, if we want to ensure we track "Face Up" as distraction even at start:
        // logic in observeFlipDetector handles: if (!isFaceDown && isRunning) -> autoPause.
        // So it should trigger automatically once isRunning becomes true.
        
        // Fetch Location if not already fetched
        if (startLatitude == null) {
             locationProvider?.fetchOneTimeLocation(
                onSuccess = { lat, lon ->
                    startLatitude = lat
                    startLongitude = lon
                },
                onError = {
                    // Ignore or log error
                }
            )
        }
        
        // Check immediate flip state?
        // NO, user requested grace period.
        // We set grace period and then check after delay.
        
        timerJob = viewModelScope.launch {
            // Grace Period Logic
            isGracePeriod = true
            // Optional: User could see "Get Ready..." or just normal timer start.
            // Let's just let it run but suppress distractions.
            
            // Wait 3 seconds
            var graceTime = 3
            while (graceTime > 0 && isRunning) {
                delay(1000)
                graceTime--
                elapsedSeconds++
                formattedTime = formatSeconds(elapsedSeconds)
                updateRemainingTime()
            }
            
            // End Grace Period
            isGracePeriod = false
            
            // Immediate check AFTER grace period
            // If they are STILL face up, distract!
             if (isRunning && flipDetector != null && !flipDetector.isFaceDown.value) {
                handleDistraction()
            }
            
            while (isRunning) {
                delay(1000)
                elapsedSeconds++
                formattedTime = formatSeconds(elapsedSeconds)
                updateRemainingTime()
            }
        }
    }

    // ... 

    fun stopTimer(onComplete: () -> Unit = {}) {
        isRunning = false
        timerJob?.cancel()

        if (elapsedSeconds > 0 && materiId != 0) {
            val endTimestamp = System.currentTimeMillis()
            val durationMs = elapsedSeconds * 1000L

            // Save to database
            viewModelScope.launch {
                try {
                    val session = StudySessionEntity(
                        userId = userId,
                        materiId = materiId,
                        materiName = materiName,
                        startTime = startTimestamp,
                        endTime = endTimestamp,
                        durationMs = durationMs,
                        latitude = startLatitude,
                        longitude = startLongitude,
                        distractionCount = distractionCount
                    )
                    studySessionRepository.saveSession(session)

                    // Update cumulative total seconds and progress
                    val db = StudymateDatabase.getDatabase(context)
                    val materiRepository = MateriRepository(db.materiDao())
                    
                    // Update total seconds
                    materiRepository.updateMateriTotalSeconds(materiId, durationMs)
                    
                    // Recalculate progress
                    val totalDurationMs = studySessionRepository.getTotalDurationForMateri(materiId)
                    val totalHours = totalDurationMs / (1000.0 * 60 * 60)
                    
                    val materi = db.materiDao().getMateriByIdOnce(materiId)
                    materi?.let {
                        val targetHours = parseTargetTime(it.targetTime)
                        val progressPercentage = if (targetHours > 0) {
                            ((totalHours / targetHours) * 100).toInt().coerceIn(0, 100)
                        } else 100
                        materiRepository.updateMateriProgress(materiId, progressPercentage)
                    }

                    // addSessionToList not needed if we observe Flow from DB!
                    // But if we want instant UI update before DB emits... Flow is better.
                    // We can remove manual list manipulation.
                    
                    elapsedSeconds = 0
                    targetReached = false
                    startLatitude = null
                    startLongitude = null 
                    distractionCount = 0 // Reset
                    onComplete()
                } catch (e: Exception) {
                    elapsedSeconds = 0
                    onComplete()
                }
            }
        } else {
            elapsedSeconds = 0
            onComplete()
        }
    }

    // Removed addSessionToList as we are now loading from DB


    private fun formatSeconds(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return String.format("%02d:%02d:%02d", h, m, s)
    }

    private fun parseTargetTime(targetTime: String): Double {
        return try {
            if (targetTime.contains(":")) {
                val parts = targetTime.split(":")
                val h = parts[0].toDoubleOrNull() ?: 0.0
                val m = if (parts.size > 1) parts[1].toDoubleOrNull() ?: 0.0 else 0.0
                val s = if (parts.size > 2) parts[2].toDoubleOrNull() ?: 0.0 else 0.0
                h + (m / 60.0) + (s / 3600.0)
            } else {
                targetTime.replace("[^0-9]".toRegex(), "").toDoubleOrNull() ?: 1.0
            }
        } catch (e: Exception) {
            1.0
        }
    }
}

