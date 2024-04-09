package com.example.zeroui.speech

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FrontViewPage(
    onGazeClick: () -> Unit,
    onGestureClick: () -> Unit,
    onMotionClick: () -> Unit,
    onSpeechRecognitionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Green Light",
            style = TextStyle(
                fontSize = 36.sp,
                color = Color.Green,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Text(
            text = "Choose an action to start",
            style = TextStyle(
                fontSize = 24.sp,
                color = Color.Green,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = { onGazeClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Start Gaze Recognition")
        }
        Button(
            onClick = { onGestureClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Start Gesture Recognition")
        }
        Button(
            onClick = { onMotionClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Start Motion Recognition")
        }
        Button(
            onClick = { onSpeechRecognitionClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Start Speech Recognition")
        }
    }
}

@Preview
@Composable
fun FrontViewPagePreview() {
    FrontViewPage(
        onGazeClick = {},
        onGestureClick = {},
        onMotionClick = {},
        onSpeechRecognitionClick = {}
    )
}
