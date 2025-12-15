package com.example.studymate.uiux.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SessionViewModel : ViewModel() {

    interface SessionController {
        fun startSession()
        fun pauseSession(auto: Boolean = false)
        fun resumeSession(auto: Boolean = false)
    }

    // ================= TIMER STATE =================
    private var timerJob: Job? = null

    var elapsedSeconds by mutableStateOf(0)
        private set

    var isRunning by mutableStateOf(false)
        private set

    val formattedTime: String
        get() {
            val h = elapsedSeconds / 3600
            val m = (elapsedSeconds % 3600) / 60
            val s = elapsedSeconds % 60
            return String.format("%02d : %02d : %02d", h, m, s)
        }

    // ================= SESSION LIST STATE =================
    var sessionList by mutableStateOf(listOf<SessionUiState>())
        private set

    // ================= TIMER ACTIONS =================

    var isAutoPaused by mutableStateOf(false)
        private set

    fun startTimer() {
        if (isRunning) return
        isRunning = true

        timerJob = viewModelScope.launch {
            while (isRunning) {
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    fun pauseTimer() {
        isRunning = false
        timerJob?.cancel()
    }

    fun stopTimer(materiName: String = "Unknown") {
        isRunning = false
        timerJob?.cancel()

        if (elapsedSeconds > 0) {
            addSession(materiName)
        }

        elapsedSeconds = 0
    }

    fun autoPauseTimer() {
        if (!isRunning) return
        isAutoPaused = true
        pauseTimer()
    }

    fun autoResumeTimer() {
        if (isRunning || !isAutoPaused) return
        isAutoPaused = false
        startTimer()
    }


    // ================= SESSION HANDLING =================
    private fun addSession(materiName: String) {
        val duration = formattedTime
        val date = SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date())

        val newSession = SessionUiState(
            materiName = materiName,
            duration = duration,
            date = date
        )

        sessionList = sessionList + newSession
    }
}
