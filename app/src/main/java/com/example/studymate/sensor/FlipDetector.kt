package com.example.studymate.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FlipDetector(context: Context) : SensorEventListener {
    

    private val _isFaceDown = MutableStateFlow(false)

    fun start() {
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}
