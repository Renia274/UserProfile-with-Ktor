package com.example.practice.screens.messaging.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun RecipientDialog(
    recipientOptions: List<Pair<String, Int>>, // Pair<String, Int> holds recipient name and corresponding drawable resource id
    onRecipientSelected: (String, Int) -> Unit, // Updated to include image resource id
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select Recipient") },
        confirmButton = {},
        dismissButton = {},
        text = {
            Column {
                recipientOptions.forEach { (option, drawableResId) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onRecipientSelected(option, drawableResId) } // Passing both name and drawableResId
                    ) {
                        Image(
                            painter = painterResource(id = drawableResId),
                            contentDescription = "Recipient Image: $option",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    )
}
