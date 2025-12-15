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
import kotlinx.coroutines.launch
import androidx.activity.viewModels

class MainActivity : ComponentActivity() {

    private lateinit var flipDetector: FlipDetector
    private val sessionViewModel: SessionViewModel by viewModels()

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
