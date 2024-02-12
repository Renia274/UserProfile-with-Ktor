package com.example.practice.screens.messaging.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusIndicator(
    selectedStatus: String,
    onClick: () -> Unit
) {
    val color = when (selectedStatus.lowercase()) {
        "online" -> Color.Green
        "busy" -> Color.Red
        "away" -> Color.Yellow
        "offline" -> Color.Gray
        else -> Color.Transparent
    }

    Box(
        modifier = Modifier
            .size(16.dp)
            .background(color = color, shape = CircleShape)
            .clickable(onClick = onClick)
    )
}

