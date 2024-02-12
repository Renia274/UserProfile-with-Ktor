package com.example.practice.screens.userprofile.profile.components

import android.os.Bundle
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.practice.logs.app.AppLogger
import com.example.practice.screens.userprofile.editprofile.components.InterestsDropDownList

@Composable
fun EditInterestsSection(
    selectedInterests: List<String>,
    onInterestsValueChange: (List<String>) -> Unit,
    isInterestsDropDownListVisible: Boolean,
    onInterestsDropDownClick: () -> Unit
) {
    // Function to log interests input events
    fun logInterestsInput(interests: List<String>) {
        val params = Bundle().apply {
            putStringArrayList("interests_input", ArrayList(interests))
        }
        AppLogger.logEvent("interests_input", params)
    }

    // Function to log crashes when handling interests selection
    fun logInterestsSelectionCrash(exception: Exception) {
        AppLogger.logError("Interests selection crash", exception)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = selectedInterests.joinToString(", "),
            onValueChange = { newInterests ->
                try {
                    onInterestsValueChange(newInterests.split(", ").map { it.trim() })
                    // Log interests input
                    logInterestsInput(newInterests.split(", ").map { it.trim() })
                } catch (e: Exception) {
                    // Log crash when handling interests selection
                    logInterestsSelectionCrash(e)
                    // Rethrow the exception to propagate the crash
                    throw e
                }
            },
            label = { Text("Enter Interests") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                IconButton(onClick = {
                    onInterestsDropDownClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        )

        // Show the InterestsDropDownList when isInterestsDropDownListVisible is true
        if (isInterestsDropDownListVisible) {
            InterestsDropDownList(
                onInterestsSelected = { selectedOptions ->
                    try {
                        // Handle the selected options
                        onInterestsValueChange(selectedOptions)
                    } catch (e: Exception) {
                        // Log crash when handling interests selection
                        logInterestsSelectionCrash(e)
                        // Rethrow the exception to propagate the crash
                        throw e
                    }
                },
                selectedInterests = selectedInterests,
                expanded = isInterestsDropDownListVisible,
                onDismissRequest = {
                    // Close the InterestsDropDownList when dismissed
                    onInterestsDropDownClick()
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}
