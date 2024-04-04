package com.example.zeroui

import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class StepCounterViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var stepCounterSensor: Sensor? = null
    private var initialStepCount: Float? = null  // To store the initial step count

    var steps by mutableFloatStateOf(0f)
        private set

    init {
        sensorManager = application.getSystemService(SensorManager::class.java)
        stepCounterSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    fun startListening() {
        sensorManager?.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            if (initialStepCount == null) {
                // This will set the initial step count the first time a step event is received.
                initialStepCount = event.values.first()
            }
            // Update steps to reflect the number of steps taken since `startListening()` was called.
            // This is done by subtracting the initial step count from the current step count.
            steps = event.values.first() - (initialStepCount ?: 0f)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}


