package com.example.zeroui.speech

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.zeroui.R



// Composable function for the recording button
@Composable
fun RecordingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Callback function to handle button click
) {
    val context = LocalContext.current

    var isRecordingInProgress by remember { mutableStateOf(false) }

    // Launcher for speech recognition activity result
    val speechRecognizerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            matches?.firstOrNull()?.let { command ->
                when {
                    command.contains("Start Motion Recognition", true) -> {
                        // Call function to start motion recognition
                    }
                    else -> {
                        // Handle unknown command
                        Toast.makeText(context, "Unknown command: $command", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        isRecordingInProgress = false // Stop recording after receiving result
    }

    Box(
        modifier = modifier.size(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clickable {
                    if (!isRecordingInProgress) {
                        try {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                            intent.putExtra(
                                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                            )
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                            speechRecognizerLauncher.launch(intent)
                            isRecordingInProgress = true // Start recording when button is clicked

                            // Timer to stop recording after 10 seconds
                            Handler(Looper.getMainLooper()).postDelayed({
                                isRecordingInProgress = false
                            }, 10000)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(context, "Speech recognition not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                strokeWidth = 4.dp,
                color = Color.Red,
                modifier = Modifier.size(48.dp),
                // Rotate the indicator during the recording
                progress = animateFloatAsState(
                    targetValue = if (isRecordingInProgress) 1f else 0f,
                    animationSpec = tween(durationMillis = 10000), label = ""
                ).value
            )
            Icon(
                painter = painterResource(id = R.drawable.audio_recording),
                contentDescription = "Recording Icon",
                tint = Color.Green,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}


@Preview
@Composable
fun RecordingButtonPreview() {
    RecordingButton(onClick = {})
}
