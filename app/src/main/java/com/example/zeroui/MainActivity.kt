package com.example.zeroui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zeroui.speech.FrontViewPage
import com.example.zeroui.ui.theme.ZeroUITheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Define the required permissions
        val requiredPermissions: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACTIVITY_RECOGNITION
        )

        // Register for permission request result
        val requestPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { entry ->
                    if (entry.value) {
                        Log.d("PermissionGranted", "Permission ${entry.key} is granted")
                    } else {
                        Log.d("PermissionDenied", "Permission ${entry.key} is denied")
                        // You can handle the scenario where a permission is denied here
                    }
                }
                // Continue with your app's main logic
                setContent {
                    ZeroUITheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Navigation(this@MainActivity)
                        }
                    }
                }
            }

        // Check if permissions are missing, and if so, request them
        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            // Request missing permissions
            requestPermissionsLauncher.launch(missingPermissions.toTypedArray())
        } else {
            // If all required permissions are already granted, proceed with the app's main logic
            setContent {
                ZeroUITheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Navigation(this@MainActivity)
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(context: Context) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Front View") {
        composable("Front View") {
            FrontViewPage(
                onGazeClick = { navController.navigate("CameraScreen") },
                onGestureClick = { fragmentActivity(context) },
                onMotionClick = { navController.navigate("Motion Sensors") },
            )
        }
        composable("Motion Sensors") { MotionSensorView(navController) }
        composable("CameraScreen") { CameraScreenView(navController) }
        //composable("") {  }
    }
}

fun fragmentActivity(context: Context) {
    val intent = Intent(context, MainActivity2::class.java)
    context.startActivity(intent)
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
