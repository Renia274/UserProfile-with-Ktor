package com.example.practice.screens.messaging

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.data.message.Message
import com.example.practice.screens.messaging.components.MessageList
import com.example.practice.screens.messaging.components.RecipientDialog
import com.example.practice.screens.messaging.components.StatusDropdown
import com.example.practice.screens.messaging.components.StatusIndicator
import com.example.practice.ui.theme.PracticeTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagingScreen(
    messages: List<Message>,
    onSendMessage: (String, String) -> Unit,
    sender: String,
    onBackClicked: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    var recipient by remember { mutableStateOf("") }
    var isStatusDropdownOpen by remember { mutableStateOf(false) }
    var isRecipientDialogOpen by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf("Online") }

    val recipientOptions = listOf("Bob", "Alice", "Eve")

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = { Text("Chat") },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {

                    Box(
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        StatusIndicator(
                            selectedStatus = selectedStatus,
                            onClick = { isStatusDropdownOpen = !isStatusDropdownOpen }
                        )
                        if (isStatusDropdownOpen) {
                            StatusDropdown(
                                selectedStatus = selectedStatus,
                                onStatusSelected = { selectedStatus = it },
                                onDismissRequest = { isStatusDropdownOpen = false }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(onClick = { isRecipientDialogOpen = true }) {
                    Text(recipient.ifBlank { "Select Recipient" })
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.TopStart) {
                MessageList(messages, sender, recipient)
            }

            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Type a message") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank() && recipient.isNotBlank()) {
                            onSendMessage(messageText, recipient)
                            messageText = ""
                        }
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }

            if (isRecipientDialogOpen) {
                RecipientDialog(
                    recipientOptions = recipientOptions,
                    onRecipientSelected = {
                        recipient = it
                        isRecipientDialogOpen = false
                    },
                    onDismissRequest = { isRecipientDialogOpen = false }
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewMessagingScreen() {
    val messages = listOf(
        Message("Hello, Alice!", "Bob"),
        Message("Hi !", "Alice")
    )
    PracticeTheme {
        MessagingScreen(
            messages = messages,
            onSendMessage = { _, _ -> },
            sender = "Me",
            onBackClicked = {}
        )
    }
}
