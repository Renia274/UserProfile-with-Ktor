package com.example.practice.screens.items

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel

@Composable
fun SignOutDialog(
    viewModel: CredentialsViewModel,
    onSignOut: () -> Unit,
    onDismiss: () -> Unit
) {


    AlertDialog(
        onDismissRequest = { onDismiss.invoke() },
        title = { Text("Sign Out") },
        text = { Text("Are you sure you want to sign out?") },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.performSignOut()
                    onSignOut.invoke()
                }
            ) {
                Text("Sign Out")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss.invoke() }) {
                Text("Cancel")
            }
        }
    )
}
