package com.example.practice.screens.userprofile.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserProfileButtons(
    isEditingProfession: Boolean,
    onSaveProfession: () -> Unit,
    onEditProfessionClick: () -> Unit,
    isEditingInterests: Boolean,
    onSaveInterests: () -> Unit,
    onEditInterestsClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isEditingProfession) {
            Button(
                onClick = {
                    onSaveProfession()
                    onEditProfessionClick()
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text("Save Profession", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    onEditProfessionClick()
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Edit Profession", color = Color.White)
            }
        }

        if (isEditingInterests) {
            Button(
                onClick = {
                    onSaveInterests()
                    onEditInterestsClick()
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text("Save Interests", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    onEditInterestsClick()
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )
            ) {
                Text("Edit Interests", color = Color.White)
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}
