package com.example.studymate.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FlipDetector(context: Context) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    
    // Threshold for detecting if phone is face down.
    // Z-axis should be close to -9.8 m/s^2 (gravity) when face down.
    private val FACE_DOWN_THRESHOLD = -9.0f

    private val _isFaceDown = MutableStateFlow(false)
    val isFaceDown: StateFlow<Boolean> = _isFaceDown.asStateFlow()

    fun start() {
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val z = it.values[2]
                // If Z is less than threshold (e.g. -9.5), it means face down
                val isDown = z < FACE_DOWN_THRESHOLD
                if (_isFaceDown.value != isDown) {
                    _isFaceDown.value = isDown
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No-op
    }
}
