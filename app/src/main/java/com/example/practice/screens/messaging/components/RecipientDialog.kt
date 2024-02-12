package com.example.practice.screens.messaging.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun RecipientDialog(
    recipientOptions: List<String>,
    onRecipientSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Recipient:")
            Spacer(modifier = Modifier.height(8.dp))
            recipientOptions.forEach { option ->
                Text(
                    text = option,
                    modifier = Modifier.clickable { onRecipientSelected(option) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}