package com.example.zeroui

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController



@Composable
fun GesturesView(navController: NavController){
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
               // PermissionsScreen(navController: NavController)
            }
        }
    }
}
@Composable
fun PermissionsScreen(navController: NavController) {
    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(
                context,
                "Permission request granted",
                Toast.LENGTH_LONG
            ).show()
            navController.navigate("camera")
        } else {
            Toast.makeText(
                context,
                "Permission request denied",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    val hasCameraPermission = remember {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(hasCameraPermission) {
        if (!hasCameraPermission) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            navController.navigate("camera")
        }
    }

    Text(
        text = "Checking permissions...",
        modifier = Modifier.fillMaxSize()
    )
}