package com.example.practice.screens.messaging.components



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.practice.data.message.Message
import com.example.practice.screens.messaging.components.MessageBubble

@Composable
fun MessageList(messages: List<Message>, sender: String, recipient: String) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
    ) {
        messages.forEach { message ->
            MessageBubble(message, sender, recipient)
        }
    }
}
