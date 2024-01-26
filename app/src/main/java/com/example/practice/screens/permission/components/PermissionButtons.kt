package com.example.practice.screens.permission.components

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat


@Composable
fun CameraButton(onPermissionResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        onPermissionResult(isGranted)
        if (isGranted) {
            Log.d("Camera", "Camera permission granted")
        } else {
            Log.d("Camera", "Camera permission denied")
        }
    }

    Button(
        onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Asking for permission
                launcher.launch(android.Manifest.permission.CAMERA)
            } else {
                // Permission already granted
                onPermissionResult(true)
                Log.d("Camera", "Camera permission already granted")
            }
        },
        modifier = Modifier.size(width = 100.dp, height = 40.dp)
    ) {
        Text(text = "CAMERA")
    }
}

@Composable
fun MicButton(onPermissionResult: (Boolean) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        onPermissionResult(isGranted)
        if (isGranted) {
            Log.d("Mic", "Microphone permission granted")
        } else {
            Log.d("Mic", "Microphone permission denied")
        }
    }

    Button(
        onClick = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Asking for permission
                launcher.launch(android.Manifest.permission.RECORD_AUDIO)
            } else {
                // Permission already granted
                onPermissionResult(true)
                Log.d("Mic", "Microphone permission already granted")
            }
        },
        modifier = Modifier.size(width = 100.dp, height = 40.dp)
    ) {
        Text(text = "MIC")
    }
}
