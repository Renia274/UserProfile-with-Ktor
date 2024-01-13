package com.example.practice.screens.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.R
import kotlinx.coroutines.delay


@Composable
fun CameraPermissionDialog(
    isPermissionDialogShown: Boolean,
    onDismiss: () -> Unit,
    onAllow: () -> Unit,
    onDeny: () -> Unit,
    onLater: () -> Unit
) {

    var isCameraPermissionGrantedState by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Camera Permission Required")
        },
        text = {
            Column {
                Image(
                    painter = painterResource(R.drawable.ic_camera),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable {
                            isCameraPermissionGrantedState = true
                            onAllow()
                        }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )

                Text("This feature requires camera permissions.")
                Text("Please grant the permission to continue.")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAllow()
            }) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDeny()
            }) {
                Text("Deny")
            }

            TextButton(onClick = {
                onLater()
            }) {
                Text("Later")
            }
        }
    )
}

@Composable
fun MicrophonePermissionDialog(
    isPermissionDialogShown: Boolean,
    onDismiss: () -> Unit,
    onAllow: () -> Unit,
    onDeny: () -> Unit,
    onLater: () -> Unit
) {


    var isMicrophonePermissionGrantedState by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Microphone Permission Required")
        },
        text = {
            Column {
                Image(
                    painter = painterResource(R.drawable.ic_microphone),
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable {
                            isMicrophonePermissionGrantedState = true
                            onAllow()
                        }
                        .background(
                            MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )

                Text("This feature requires audio permissions.")
                Text("Please grant the permission to continue.")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAllow()
            }) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDeny()
            }) {
                Text("Deny")
            }

            TextButton(onClick = {
                onLater()
            }) {
                Text("Later")
            }
        }
    )
}

@Composable
fun PermissionDialog(
    isPermissionDialogShown: Boolean,
    onDismiss: () -> Unit,
    onAllowCamera: () -> Unit,
    onDenyCamera: () -> Unit,
    onLaterCamera: () -> Unit,
    onAllowMicrophone: () -> Unit,
    onDenyMicrophone: () -> Unit,
    onLaterMicrophone: () -> Unit
) {
    var isCameraPermissionGrantedState by remember { mutableStateOf(false) }
    var isMicrophonePermissionGrantedState by remember { mutableStateOf(false) }

    // delay between the dialogs
    LaunchedEffect(Unit) {
        delay(1000)
    }

    if (isPermissionDialogShown) {
        // Display camera permission dialog
        CameraPermissionDialog(
            isPermissionDialogShown = !isMicrophonePermissionGrantedState,
            onDismiss = { onDismiss() },
            onAllow = { onAllowCamera() },
            onDeny = { onDenyCamera() },
            onLater = { onLaterCamera() }
        )

        // Display microphone permission dialog
        MicrophonePermissionDialog(
            isPermissionDialogShown = !isCameraPermissionGrantedState,
            onDismiss = { onDismiss() },
            onAllow = { onAllowMicrophone() },
            onDeny = { onDenyMicrophone() },
            onLater = { onLaterMicrophone() }
        )
    }
}


@Composable
@Preview
fun PermissionDialogPreview() {
    MaterialTheme {
        PermissionDialog(
            isPermissionDialogShown = true,
            onDismiss = { /*  */ },
            onAllowCamera = { /*  */ },
            onDenyCamera = { /*  */ },
            onLaterCamera = { /*  */ },
            onAllowMicrophone = { /*  */ },
            onDenyMicrophone = { /*  */ },
            onLaterMicrophone = { /*  */ }
        )
    }
}