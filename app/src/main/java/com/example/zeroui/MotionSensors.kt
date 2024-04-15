package com.example.zeroui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.math.*

@Composable
fun MotionSensorView(navController: NavController) {
    // Bottom bar navigation and calls Displays that shows on the view
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { navController.navigate("") }) {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription = "Takes you to the Front page"
                            )
                        }
                        IconButton(onClick = { navController.navigate("Motion Sensors") }) {
                            Icon(
                                Icons.Filled.Build,
                                contentDescription = "Takes you to the motion sensors page",
                            )
                        }
                        IconButton(onClick = { navController.navigate("") }) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Takes you to the ? page",
                            )
                        }
                        IconButton(onClick = {  }) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Takes you to the ? page",
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier

                .padding(innerPadding) // Apply padding from the Scaffold
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(horizontal = 16.dp) // Apply additional horizontal padding if needed
            ) {
                StepCounterApp()
                Spacer(modifier = Modifier)
                RotationDegree()
            }
        }
    }
}

@Composable//uses ViewModel to do its thing and calls StepCounterDisplay
fun StepCounterApp() {
    val stepCounterViewModel: StepCounterViewModel = viewModel()

    // Observe the steps count as a state and correctly reference
    val steps = stepCounterViewModel.steps.absoluteValue


    DisposableEffect(key1 = Unit) {

        stepCounterViewModel.startListening()


        onDispose {
            stepCounterViewModel.stopListening()
        }
    }


    StepCounterDisplay(steps)
}

@Composable //Display steps taken and indicator
fun StepCounterDisplay(steps: Float) { // Accepts steps directly

    Box(contentAlignment = Alignment.Center) {
        // Use the steps value directly in your Text composable
        CircularProgressIndicator(
            progress = steps / 100,
            color = Color(0xFFF44336),
            strokeWidth = 8.dp, // Specify the stroke width here directly
            modifier = Modifier.size(200.dp)
        )
        Text(text = "Steps: ${steps.toInt()}/100")
    }
}

@Composable
fun RotationDegree(){
    val sensorManager = getSystemService(LocalContext.current, SensorManager::class.java) as SensorManager
    val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Extract rotation vector data from SensorEvent
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            // Get the device orientation
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)

            // Convert radians to degrees
            val azimuthDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
            val pitchDegrees = Math.toDegrees(orientation[1].toDouble()).toFloat()
            val rollDegrees = Math.toDegrees(orientation[2].toDouble()).toFloat()

            // Update UI with the rotation data
            // You can pass these values to your custom composable or draw them directly here
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Will be implemented in the future
        }
    }

    // Register the sensor listener when the composable is created
    LocalContext.current.resources
    sensorManager.registerListener(sensorListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)

    // Unregister the sensor listener when the composable is removed
    DisposableEffect(Unit) {
       onDispose {
           sensorManager.unregisterListener(sensorListener)
       }
    }
}

@Composable
fun DrawRotationVector(azimuth: Float, pitch: Float, roll: Float) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = 100f

        drawCircle(color = Color.Blue, radius = radius, center = Offset(centerX, centerY))

        // Draw lines representing device orientation
        val lineLength = 2 * radius
        val azimuthLineX = centerX + lineLength * sin(azimuth)
        val azimuthLineY = centerY - lineLength * cos(azimuth)
        drawLine(color = Color.Red, start = Offset(centerX, centerY), end = Offset(azimuthLineX, azimuthLineY), strokeWidth = 5f)

        val pitchLineX = centerX + lineLength * sin(pitch)
        val pitchLineY = centerY - lineLength * cos(pitch)
        drawLine(color = Color.Green, start = Offset(centerX, centerY), end = Offset(pitchLineX, pitchLineY), strokeWidth = 5f)

        val rollLineX = centerX + lineLength * sin(roll)
        val rollLineY = centerY - lineLength * cos(roll)
        drawLine(color = Color.Yellow, start = Offset(centerX, centerY), end = Offset(rollLineX, rollLineY), strokeWidth = 5f)
    }
}

@Preview
@Composable
fun PreviewRotationVector() {
    DrawRotationVector(azimuth = 0f, pitch = 0f, roll = 0f)
}
