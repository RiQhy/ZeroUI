package com.example.zeroui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.zeroui.speech.FrontViewPage
import com.example.zeroui.ui.theme.ZeroUITheme


class MainActivity : ComponentActivity() {
    val viewModel: ViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (!isGranted) {
            // Inform the user that the permission is needed.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionsGranted = HashMap<String, Boolean>()
        val requiredPermissions: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO, // Add RECORD_AUDIO permission
            Manifest.permission.ACTIVITY_RECOGNITION // Add ACTIVITY_RECOGNITION permission
        )

        // Request permissions for requiredPermissions
        val requestPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach { entry ->
                    if (entry.value) {
                        permissionsGranted[entry.key] = entry.value
                        Log.d("PermissionGranted", "Permission ${entry.key} is granted")
                    } else {
                        Log.d("PermissionDenied", "Permission ${entry.key} is denied")
                    }
                }
            }

        // Launch permission request
        requestPermissionsLauncher.launch(requiredPermissions)

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
                onGazeClick = { navController.navigate("CameraScreen") },
                onGestureClick = { navController.navigate("GestureDestination") },
                onMotionClick = { navController.navigate("Motion Sensors") },
            )
        }
        composable("Motion Sensors") { MotionSensorView(navController) }
        composable("CameraScreen") { CameraScreen() }
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
