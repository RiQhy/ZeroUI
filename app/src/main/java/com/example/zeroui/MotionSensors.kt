package com.example.zeroui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.math.absoluteValue

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
                        IconButton(onClick = { navController.navigate("Gesture Recognizer") }) {
                            Icon(
                                Icons.Filled.Home,
                                contentDescription = "Takes you to the ? page"
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