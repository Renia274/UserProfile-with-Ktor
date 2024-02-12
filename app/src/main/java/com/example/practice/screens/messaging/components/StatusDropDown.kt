package com.example.practice.screens.messaging.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StatusDropdown(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth()
    ) {
        listOf("Online", "Busy", "Away", "Offline").forEach { option ->
            DropdownMenuItem(onClick = {
                onStatusSelected(option)
                onDismissRequest()
            }) {
                Text(text = option)
            }
        }
    }
}