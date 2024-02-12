package com.example.practice.screens.messaging.components

import com.example.practice.data.message.Message
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun MessageBubble(message: Message, sender: String, recipient: String) {
    val isSender = message.sender == sender
    val bubbleAlignment = if (isSender) Alignment.TopStart else Alignment.TopEnd
    val backgroundColor = if (isSender) Color.Blue else Color.LightGray
    val contentColor = if (isSender) Color.White else Color.Black

    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp),
        contentAlignment = bubbleAlignment
    ) {
        Column {
            Text(
                text = if (isSender) "Me" else message.sender,
                color = contentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = message.text,
                color = contentColor
            )

            Spacer(modifier = Modifier.height(8.dp))

        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

