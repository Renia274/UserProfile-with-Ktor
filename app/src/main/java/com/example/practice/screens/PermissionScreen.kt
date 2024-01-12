package com.example.practice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practice.profiles.viewmodel.permissions.PermissionStateViewModel
import com.example.practice.screens.items.CameraButton
import com.example.practice.screens.items.CameraPermissionDialog
import com.example.practice.screens.items.MicButton
import com.example.practice.screens.items.MicrophonePermissionDialog

@Composable
fun PermissionScreen(
    permissionStateViewModel: PermissionStateViewModel,
    onBackButtonClick: () -> Unit
) {
    val permissionState by permissionStateViewModel.permissionState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Permissions") },
            navigationIcon = {
                IconButton(onClick = { onBackButtonClick() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show CameraPermissionDialog if camera permissions are not granted
        if (permissionState.isCameraPermissionDialogShown) {
            CameraPermissionDialog(
                isPermissionDialogShown = permissionState.isCameraPermissionDialogShown,
                onDismiss = {
                    permissionStateViewModel.setCameraPermissionDialogShown(false)
                },
                onAllow = {
                    // Handle camera permission granted
                    permissionStateViewModel.setCameraPermissionDialogShown(false)
                },
                onDeny = {
                    // Handle camera permission denied
                    permissionStateViewModel.setCameraPermissionDialogShown(false)
                },
                onLater = {
                    // Handle camera permission later
                    permissionStateViewModel.setCameraPermissionDialogShown(false)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show MicrophonePermissionDialog if microphone permissions are not granted
        if (permissionState.isMicrophonePermissionDialogShown) {
            MicrophonePermissionDialog(
                isPermissionDialogShown = permissionState.isMicrophonePermissionDialogShown,
                onDismiss = {
                    permissionStateViewModel.setMicrophonePermissionDialogShown(false)
                },
                onAllow = {
                    // Handle microphone permission granted
                    permissionStateViewModel.setMicrophonePermissionDialogShown(false)
                },
                onDeny = {
                    // Handle microphone permission denied
                    permissionStateViewModel.setMicrophonePermissionDialogShown(false)
                },
                onLater = {
                    // Handle microphone permission later
                    permissionStateViewModel.setMicrophonePermissionDialogShown(false)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "PERMISSIONS FOR THE APP",
            style = MaterialTheme.typography.h6,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Click the camera button for camera permission. ",
            style = MaterialTheme.typography.body1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Click the mic button for microphone  permission. ",
            style = MaterialTheme.typography.body1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Camera and Mic buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CameraButton(permissionStateViewModel)
            Spacer(modifier = Modifier.width(64.dp))
            MicButton(permissionStateViewModel)
        }
    }
}
