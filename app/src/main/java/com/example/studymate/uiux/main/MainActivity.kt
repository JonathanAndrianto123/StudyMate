package com.example.studymate.uiux.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.studymate.location.LocationProvider
import com.example.studymate.location.PlaceResolver
import com.example.studymate.sensor.FlipDetector
import com.example.studymate.ui.theme.StudyMateTheme
import com.example.studymate.uiux.session.SessionViewModel
import com.example.studymate.uiux.session.SessionViewModelFactory
import kotlinx.coroutines.launch
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {

    private lateinit var flipDetector: FlipDetector
    private val sessionViewModel: SessionViewModel by viewModels {
        val userPrefs = com.example.studymate.util.UserPreferences(applicationContext)
        val db = com.example.studymate.data.local.StudymateDatabase.getDatabase(applicationContext)
        val repository = com.example.studymate.data.repository.StudySessionRepository(db.studySessionDao())
        com.example.studymate.uiux.session.SessionViewModelFactory(
            applicationContext,
            repository,
            userPrefs.getCurrentUserId()
        )
    }

    private var lastFlipTs = 0L
    private val DEBOUNCE_MS = 1200L

    private fun canToggle(): Boolean {
        val now = System.currentTimeMillis()
        return if (now - lastFlipTs > DEBOUNCE_MS) {
            lastFlipTs = now
            true
        } else {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MainNavGraph(navController = navController)
        }

        flipDetector = FlipDetector(this)

        lifecycleScope.launch {
            flipDetector.isFaceDown.collect { isDown ->
                if (!canToggle()) return@collect

                if (isDown) {
                    sessionViewModel.autoPauseTimer()
                } else {
                    sessionViewModel.autoResumeTimer()
                }
            }
        }

        scheduleStudyReminders()
    }

    private fun scheduleStudyReminders() {
        val workRequest = androidx.work.PeriodicWorkRequestBuilder<com.example.studymate.worker.StudyReminderWorker>(
            24, java.util.concurrent.TimeUnit.HOURS
        ).build()

        androidx.work.WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "STUDY_REMINDER_WORK",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    override fun onResume() {
        super.onResume()
        flipDetector.start()
    }

    override fun onPause() {
        super.onPause()
        flipDetector.stop()
    }
}
