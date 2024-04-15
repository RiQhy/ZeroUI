package com.example.zeroui

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions


@Composable
fun CameraScreen() {
    val faceState = remember { mutableStateOf(FaceState.NO_FACE) }
    CameraPreview(faceState)

    Box(modifier = Modifier.fillMaxSize().background(color = when (faceState.value) {
        FaceState.FACE_OPEN_EYES -> Color.Green
        FaceState.FACE_CLOSED_EYES -> Color.Black
        FaceState.NO_FACE -> Color.Red
    }))
}

enum class FaceState {
    FACE_OPEN_EYES, FACE_CLOSED_EYES, NO_FACE
}


@Composable
fun CameraPreview(faceState: MutableState<FaceState>) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    setupCamera(cameraProviderFuture, previewView, context, lifecycleOwner, faceState)

    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    )
}

private fun setupCamera(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    faceState: MutableState<FaceState>
) {
    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(context.mainExecutor) { imageProxy ->
                    processImageProxy(imageProxy, faceState)
                }
            }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageAnalysis)
        } catch (exc: Exception) {
            Log.e("CameraScreen", "Binding camera use cases failed", exc)
        }
    }, ContextCompat.getMainExecutor(context))
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun  processImageProxy(imageProxy: ImageProxy, faceState: MutableState<FaceState>) {
    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)
        val detector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        )

        detector.process(image)
            .addOnSuccessListener { faces ->
                val newState = when {
                    faces.any { (it.leftEyeOpenProbability ?: -1f) > 0.75 || (it.rightEyeOpenProbability ?: -1f) > 0.75 } -> FaceState.FACE_OPEN_EYES
                    faces.any { (it.leftEyeOpenProbability ?: -1f) < 0.5 && (it.rightEyeOpenProbability ?: -1f) < 0.5 } -> FaceState.FACE_CLOSED_EYES
                    else -> FaceState.NO_FACE
                }
                faceState.value = newState
            }
            .addOnCompleteListener { imageProxy.close() }
    } else {
        Log.e("CameraPreview", "No image available from imageProxy.")
        imageProxy.close()
    }
}

