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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionViewModel(
    private val context: Context,
    private val studySessionRepository: StudySessionRepository,
    private val userId: Int
) : ViewModel() {

    var isRunning by mutableStateOf(false)
    var elapsedSeconds by mutableStateOf(0L)
    var formattedTime by mutableStateOf("00:00:00")
    
    // Advanced Info
    var remainingSeconds by mutableStateOf(0L)
    var formattedRemainingTime by mutableStateOf("00:00:00")
    var targetReached by mutableStateOf(false)
    private var targetSecondsTotal = 0L
    private var initialTotalSeconds = 0L

    // Untuk tracking materi mana yg lagi dipelajari
    var materiId by mutableStateOf(0)
    var materiName by mutableStateOf("")

    private var timerJob: Job? = null
    private var startTimestamp: Long = 0

    var sessionList by mutableStateOf(listOf<SessionUiState>())

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Study Timer Channel"
            val descriptionText = "Notifications for study goal reached"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("STUDY_TIMER_CHANNEL", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendTargetReachedNotification() {
        val builder = NotificationCompat.Builder(context, "STUDY_TIMER_CHANNEL")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Fallback icon
            .setContentTitle("Goal Reached!")
            .setContentText("Congratulations! You have reached your study goal for $materiName.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    fun setMateri(id: Int, name: String) {
        materiId = id
        materiName = name
        
        // Pre-load target and total so we can calculate remaining time
        viewModelScope.launch {
            val db = StudymateDatabase.getDatabase(context)
            val materi = db.materiDao().getMateriByIdOnce(id)
            materi?.let {
                targetSecondsTotal = (parseTargetTime(it.targetTime) * 3600).toLong()
                initialTotalSeconds = it.totalSeconds.toLong()
                updateRemainingTime()
            }
        }
    }

    private fun updateRemainingTime() {
        val currentTotal = initialTotalSeconds + elapsedSeconds
        val remaining = (targetSecondsTotal - currentTotal).coerceAtLeast(0)
        remainingSeconds = remaining
        formattedRemainingTime = formatSeconds(remaining)
        
        if (currentTotal >= targetSecondsTotal && !targetReached && targetSecondsTotal > 0) {
            targetReached = true
            sendTargetReachedNotification()
        }
    }

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        startTimestamp = System.currentTimeMillis()
        timerJob = viewModelScope.launch {
            while (isRunning) {
                delay(1000)
                elapsedSeconds++
                formattedTime = formatSeconds(elapsedSeconds)
                updateRemainingTime()
            }
        }
    }

    fun pauseTimer() {
        isRunning = false
        timerJob?.cancel()
    }

    fun autoPauseTimer() {
        pauseTimer()
    }

    fun autoResumeTimer() {
        startTimer()
    }

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
                        durationMs = durationMs
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

                    addSessionToList(materiName, formattedTime)
                    
                    elapsedSeconds = 0
                    targetReached = false
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

    private fun addSessionToList(name: String, time: String) {
        val newList = sessionList.toMutableList()
        newList.add(0, SessionUiState(name, time, SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())))
        sessionList = newList
    }

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

