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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.math.*

@Composable
fun MotionSensorView(navController: NavController) {
    // Bottom bar navigation and calls Displays that shows on the view
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = {
            BottomAppBar(
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { navController.navigate("Gesture Recognizer") }) {
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
                .fillMaxSize()
                .verticalScroll(scrollState)
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

    var azimuthDegrees by remember { mutableStateOf(0f) }
    var pitchDegrees by remember { mutableStateOf(0f) }
    var rollDegrees by remember { mutableStateOf(0f) }

    val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            // Extract rotation vector data from SensorEvent
            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

            // Get the device orientation
            val orientation = FloatArray(3)
            SensorManager.getOrientation(rotationMatrix, orientation)

            // Convert radians to degrees
            azimuthDegrees = Math.toDegrees(orientation[0].toDouble()).toFloat()
            pitchDegrees = Math.toDegrees(orientation[1].toDouble()).toFloat()
            rollDegrees = Math.toDegrees(orientation[2].toDouble()).toFloat()
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Will be implemented in the future
        }
    }

    // Register the sensor listener when the composable is created
    DisposableEffect(Unit) {
        sensorManager.registerListener(sensorListener, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }

    // Draw the rotation vector based on the current state
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DrawRotationVectorAzimuth(azimuthDegrees)
        DrawRotationVectorPitch(pitchDegrees)
        DrawRotationVectorRoll(rollDegrees)
    }
}

@Composable
fun DrawRotationVectorAzimuth(azimuthDegrees: Float) {
    Column {
        Text("$azimuthDegrees")
    }
    Canvas(
        modifier = Modifier.size(200.dp),
        onDraw = {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = 100f

            drawCircle(color = Color.Blue, radius = radius, center = Offset(centerX, centerY))

            // Draw lines representing device orientation
            val lineLength = 2 * radius
            val azimuthRadians = Math.toRadians(azimuthDegrees.toDouble())
            val azimuthLineX = centerX + lineLength * sin(azimuthRadians).toFloat()
            val azimuthLineY = centerY - lineLength * cos(azimuthRadians).toFloat()
            drawLine(color = Color.Red, start = Offset(centerX, centerY), end = Offset(azimuthLineX, azimuthLineY), strokeWidth = 5f)
        }
    )
}

@Composable
fun DrawRotationVectorPitch(pitchDegrees: Float) {
    Column {
        Text("$pitchDegrees")
    }
    Canvas(
        modifier = Modifier.size(200.dp),
        onDraw = {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = 100f

            drawCircle(color = Color.Blue, radius = radius, center = Offset(centerX, centerY))

            // Draw lines representing device orientation
            val lineLength = 2 * radius

            val pitchRadians = Math.toRadians(pitchDegrees.toDouble())
            val pitchLineX = centerX + lineLength * sin(pitchRadians).toFloat()
            val pitchLineY = centerY - lineLength * cos(pitchRadians).toFloat()
            drawLine(color = Color.Green, start = Offset(centerX, centerY), end = Offset(pitchLineX, pitchLineY), strokeWidth = 5f)
        }
    )
}

@Composable
fun DrawRotationVectorRoll(rollDegrees: Float) {
    Column {
        Text("$rollDegrees")
    }
    Canvas(
        modifier = Modifier.size(200.dp),
        onDraw = {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = 100f

            drawCircle(color = Color.Blue, radius = radius, center = Offset(centerX, centerY))

            // Draw lines representing device orientation
            val lineLength = 2 * radius

            val rollRadians = Math.toRadians(rollDegrees.toDouble())
            val rollLineX = centerX + lineLength * sin(rollRadians).toFloat()
            val rollLineY = centerY - lineLength * cos(rollRadians).toFloat()
            drawLine(color = Color.Yellow, start = Offset(centerX, centerY), end = Offset(rollLineX, rollLineY), strokeWidth = 5f)
        }
    )
}


