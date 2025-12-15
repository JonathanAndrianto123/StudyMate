package com.example.studymate.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs

class FlipDetector(context: Context) : SensorEventListener {

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private val accelerometer: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    // true = HP terbalik (layar menghadap bawah)
    private val _isFaceDown = MutableStateFlow(false)
    val isFaceDown: StateFlow<Boolean> = _isFaceDown

    private val FACE_DOWN_THRESHOLD = -9f  // z-axis menghadap bawah

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val z = event.values[2]

        val faceDownNow = z < FACE_DOWN_THRESHOLD
        if (_isFaceDown.value != faceDownNow) {
            _isFaceDown.value = faceDownNow
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // no-op
    }
}
