package com.example.practice.ktor.screens.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practice.ktor.dto.PostResponse

@Composable
fun PostItem(
    post: PostResponse,
    onUpdateClick: (PostResponse, String, String) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedTitle by remember { mutableStateOf(post.title) }
    var editedBody by remember { mutableStateOf(post.body) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {

            if (!isEditing) {
                Text(text = editedTitle, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = editedBody, style = MaterialTheme.typography.body1)
            } else {
                OutlinedTextField(
                    value = editedTitle,
                    onValueChange = {
                        editedTitle = it
                    },
                    label = { Text("Title") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editedBody,
                    onValueChange = {
                        editedBody = it
                    },
                    label = { Text("Body") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (isEditing) {
                            onUpdateClick(post, editedTitle, editedBody)
                        }
                        isEditing = !isEditing
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isEditing) "Save Changes" else "Edit Post")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        onDeleteClick(post.id)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Delete Post", color = Color.White)
                }
            }
        }
    }
}
