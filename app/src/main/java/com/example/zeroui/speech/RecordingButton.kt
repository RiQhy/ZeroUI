package com.example.zeroui.speech


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.zeroui.R

// Animation components
import androidx.compose.animation.core.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*

// Function to create a recording button
@Composable
fun RecordingButton(
    modifier: Modifier = Modifier,
    isRecordingInProgress: Boolean,
    onClick: () -> Unit,
    iconResId: Int
) {
    // Set colors for the button and icon
    val buttonColor = if (isRecordingInProgress) Color.Red else Color.Red
    val iconColor = Color.Green

    // Define an animation transition
    val transition = rememberInfiniteTransition(label = "")

    // Define progress for the animation
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    // Creating the recording button
    Box(
        modifier = modifier.size(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center // Center align content within the box
        ) {
            Box(
                modifier = Modifier.size(64.dp),
                contentAlignment = Alignment.Center
            ) {
                // Circular progress indicator to show recording time
                CircularProgressIndicator(
                    progress = if (isRecordingInProgress) progress else 0f,
                    strokeWidth = 4.dp,
                    color = buttonColor,
                    modifier = Modifier.size(48.dp)
                )
                // Icon for the recording button
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Recording Icon",
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

// Preview function to see how the recording button looks
@Preview
@Composable
fun RecordingButtonPreview() {
    // Preview the button with recording in progress
    RecordingButton(
        isRecordingInProgress = true,
        onClick = {},
        iconResId = R.drawable.audio_recording
    )
}
