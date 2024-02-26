package com.example.practice.screens.permission

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.practice.profiles.viewmodel.permissions.PermissionViewModel
import com.example.practice.screens.permission.components.CameraButton
import com.example.practice.screens.permission.components.MicButton

@Composable
fun PermissionScreen(onBackButtonClick: () -> Unit, viewModel: PermissionViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(title = { Text("Permissions") }, navigationIcon = {
            IconButton(onClick = { onBackButtonClick() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })

        PermissionContent(
            onCameraPermissionResult = { isGranted -> viewModel.setCameraPermissionResult(isGranted) },
            onMicPermissionResult = { isGranted -> viewModel.setMicPermissionResult(isGranted) }
        )
    }
}



@Composable
fun PermissionContent(
    onCameraPermissionResult: (Boolean) -> Unit,
    onMicPermissionResult: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

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
            "Click the mic button for microphone permission. ",
            style = MaterialTheme.typography.body1,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CameraButton(onPermissionResult = onCameraPermissionResult)
            Spacer(modifier = Modifier.width(64.dp))
            MicButton(onPermissionResult = onMicPermissionResult)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PermissionContentPreview() {
    PermissionContent(onCameraPermissionResult = {}, onMicPermissionResult = {})
}
