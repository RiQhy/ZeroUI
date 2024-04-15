package com.example.zeroui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zeroui.speech.FrontViewPage
import com.example.zeroui.ui.theme.ZeroUITheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZeroUITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Front View") {
        composable("Front View") {
            FrontViewPage(
                onGazeClick = { navController.navigate("GazeDestination") },
                onGestureClick = { navController.navigate("GestureDestination") },
                onMotionClick = { navController.navigate("Motion Sensors") },
            )
        }
        composable("Motion Sensors") { MotionSensorView(navController) }
        composable("CameraScreen") { CameraScreen() }
        //composable("") {  }
        //composable("") {  }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZeroUITheme {
        FrontViewPage(
            onGazeClick = {},
            onGestureClick = {},
            onMotionClick = {},
        )
    }
}