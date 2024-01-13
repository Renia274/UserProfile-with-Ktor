package com.example.practice.screens.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.delay

@Composable
fun SaveConfirmationDialog(
    onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    var isSpinnerVisible by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = {
        if (!isSpinnerVisible) {
            onDismiss.invoke()
        }
    }, title = { Text(if (isSpinnerVisible) "Saving Changes" else "Save Changes") }, text = {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
        ) {
            if (isSpinnerVisible) {
                LaunchedEffect(isSpinnerVisible) {
                    delay(2000)
                    onConfirm.invoke()
                    isSpinnerVisible = false
                }
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text("Are you sure you want to save changes?")
            }
        }
    }, confirmButton = {
        TextButton(onClick = {
            isSpinnerVisible = true
        }) { Text("Confirm") }
    }, dismissButton = {
        TextButton(onClick = {
            onDismiss.invoke()
        }) {
            Text("Cancel")
        }
    })
}

@Preview
@Composable
fun SaveConfirmationDialogPreview() {
    PracticeTheme {
        SaveConfirmationDialog(
            onConfirm = { /* */ },
            onDismiss = { /*  */ }
        )
    }
}