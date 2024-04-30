package com.example.zeroui.speech


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
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
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import java.util.Locale

@Composable
fun RecordingButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onGazeClick: ()-> Unit,
    onGestureClick: ()->Unit
) {
    // Accessing the current context
    val context = LocalContext.current

    // State to track whether recording is in progress and recognized command
    var isRecordingInProgress by remember { mutableStateOf(false) }
    var recognizedCommand by remember { mutableStateOf<String?>(null) }

    // Recognition listener for speech recognition
    val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            // Display a prompt message when ready for speech
            recognizedCommand = "Speak..."
        }

        //callback methods for the recognition process
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {}
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            // Display toast message for recognition error
            Toast.makeText(context, "Recognition error", Toast.LENGTH_SHORT).show()
            isRecordingInProgress = false
        }

        override fun onResults(results: Bundle?) {
            results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
                matches.firstOrNull()?.let { command ->
                    recognizedCommand = command
                    when {
                        // Handling recognized commands
                        command.equals("Motion sensor", ignoreCase = true) -> {
                            Toast.makeText(context, "Command recognized: $command", Toast.LENGTH_SHORT).show()
                            onClick()
                        }
                        command.equals("I tracker", ignoreCase = true) -> {
                            Toast.makeText(context, "Command recognized: $command", Toast.LENGTH_SHORT).show()
                            onGazeClick()
                        }
                        command.equals("Gesture", ignoreCase = true) -> {
                            Toast.makeText(context, "Command recognized: $command", Toast.LENGTH_SHORT).show()
                            onGestureClick()
                        }
                        else -> {
                            // unrecognized command
                            Toast.makeText(context, "Unrecognized command: $command", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            isRecordingInProgress = false
        }

        // callback methods for the recognition process
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    // Initializing SpeechRecognizer with recognition listener
    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(recognitionListener)
        }
    }

    // Start listening for speech recognition
    LaunchedEffect(Unit) {
        val startListening: () -> Unit = {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak...")
            speechRecognizer.startListening(intent)
            isRecordingInProgress = true

            // Set a timeout for recognition
            Handler(Looper.getMainLooper()).postDelayed({
                if (isRecordingInProgress) {
                    // Display toast message for timeout
                    Toast.makeText(context, "No command received. Please try again.", Toast.LENGTH_SHORT).show()
                    isRecordingInProgress = false
                }
            }, 10000)
        }

        if (!isRecordingInProgress) {
            startListening()
        }
    }

    // Composable Box containing recording button elements
    Box(
        modifier = modifier.size(64.dp)
    ) {
        // Box containing circular progress indicator and recording icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .clickable {
                    if (!isRecordingInProgress) {
                        isRecordingInProgress = true
                    }
                },
            contentAlignment = Alignment.Center
        ) {

            // Animating circular progress indicator based on recording status
            val progress by animateFloatAsState(
                targetValue = if (isRecordingInProgress) 1f else 0f,
                animationSpec = tween(durationMillis = 10000), label = ""
            )
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(48.dp),
                color = Color.Red,
                strokeWidth = 4.dp,
            )
            // Recording icon
            Icon(
                painter = painterResource(id = R.drawable.audio_recording),
                contentDescription = "Recording Icon",
                tint = Color.Green,
                modifier = Modifier.size(32.dp)
            )
        }

        // Display recognized command text
        recognizedCommand?.let {
            Text(
                text = it,
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter).offset(y=32.dp)
            )
        }
    }
}


@Preview
@Composable
fun RecordingButtonPreview() {
    RecordingButton(
        onClick = {},
        onGazeClick = {},
        onGestureClick = {}
    )

}
