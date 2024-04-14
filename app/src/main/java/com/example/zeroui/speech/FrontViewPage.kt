package com.example.zeroui.speech

// Compose components
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zeroui.R



@Composable
fun FrontViewPage(
    onGazeClick: () -> Unit,
    onGestureClick: () -> Unit,
    onMotionClick: () -> Unit,
) {
    // States to keep track of button clicks and recognition status
    var isGazeButtonClicked by remember { mutableStateOf(false) }
    var isGestureButtonClicked by remember { mutableStateOf(false) }
    var isMotionButtonClicked by remember { mutableStateOf(false) }
    var isSpeechRecognitionInProgress by remember { mutableStateOf(false) }

    // UI components for the front view page
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.green_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Semi-transparent overlay to darken the background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        // Recording button for speech recognition
        RecordingButton(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .align(Alignment.TopStart),
            isRecordingInProgress = isSpeechRecognitionInProgress,
            onClick = { isSpeechRecognitionInProgress = !isSpeechRecognitionInProgress }, // Toggle recognition status
            iconResId = R.drawable.audio_recording
        )
    }

    // Column to display text and buttons
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Title text
        Text(
            text = "Green Light",
            style = TextStyle(
                fontSize = 36.sp,
                color = Color.Green,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        // Instruction text
        Text(
            text = "Choose an action to start",
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Green,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        // Button for gaze recognition
        Button(
            onClick = {
                isGazeButtonClicked = true
                onGazeClick() // Invoke callback function
            },
            modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 16.dp)
        ) {
            Text("Start Gaze Recognition")
        }
        // Button for gesture recognition
        Button(
            onClick = {
                isGestureButtonClicked = true
                onGestureClick() // Invoke callback function
            },
            modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 16.dp)
        ) {
            Text("Start Gesture Recognition")
        }
        // Button for motion recognition
        Button(
            onClick = {
                isMotionButtonClicked = true
                onMotionClick() // Invoke callback function
            },
            modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 16.dp)
        ) {
            Text("Start Motion Recognition")
        }
    }
}

// Preview function to visualize the front view page
@ExperimentalAnimationApi
@Preview
@Composable
fun FrontViewPagePreview() {
    FrontViewPage(
        onGazeClick = {},
        onGestureClick = {},
        onMotionClick = {},
    )
}