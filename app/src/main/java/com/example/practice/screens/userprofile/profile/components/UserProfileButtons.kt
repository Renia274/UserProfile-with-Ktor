package com.example.practice.screens.userprofile.profile.components

import android.os.Bundle
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
import com.example.practice.logs.app.AppLogger

@Composable
fun UserProfileButtons(
    isEditingProfession: Boolean,
    onSaveProfession: () -> Unit,
    onEditProfessionClick: () -> Unit,
    isEditingInterests: Boolean,
    onSaveInterests: () -> Unit,
    onEditInterestsClick: () -> Unit
) {
    //  log button clicks
    fun logButtonClick(buttonName: String) {
        AppLogger.logEvent("button_click", Bundle().apply {
            putString("button_name", buttonName)
        })
    }

    // log crashes when handling button clicks
    fun logButtonCrash(buttonName: String, exception: Exception) {
        AppLogger.logError("Button click crash - $buttonName", exception)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isEditingProfession) {
            Button(
                onClick = {
                    try {
                        onSaveProfession()
                        onEditProfessionClick()
                        // Log button click
                        logButtonClick("Save Profession")
                    } catch (e: Exception) {
                        // Log crash when handling button click
                        logButtonCrash("Save Profession", e)
                        // Rethrow the exception to propagate the crash
                        throw e
                    }
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text("Save Profession", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    try {
                        onEditProfessionClick()
                        // Log button click
                        logButtonClick("Edit Profession")
                    } catch (e: Exception) {
                        // Log crash when handling button click
                        logButtonCrash("Edit Profession", e)
                        // Rethrow the exception to propagate the crash
                        throw e
                    }
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
                    try {
                        onSaveInterests()
                        onEditInterestsClick()
                        // Log button click
                        logButtonClick("Save Interests")
                    } catch (e: Exception) {
                        // Log crash when handling button click
                        logButtonCrash("Save Interests", e)
                        // Rethrow the exception to propagate the crash
                        throw e
                    }
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text("Save Interests", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    try {
                        onEditInterestsClick()
                        // Log button click
                        logButtonClick("Edit Interests")
                    } catch (e: Exception) {
                        // Log crash when handling button click
                        logButtonCrash("Edit Interests", e)
                        // Rethrow the exception to propagate the crash
                        throw e
                    }
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


