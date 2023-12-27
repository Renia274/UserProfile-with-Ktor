package com.example.practice.screens.items

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practice.data.CustomWindowInfo
import com.example.practice.data.rememberWindowInfo

@Composable
fun InterestsDropDownList(
    onInterestsSelected: (List<String>) -> Unit,
    selectedInterests: List<String>,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    // List of interests/hobbies
    val interestsList = listOf(
        "Reading", "Traveling", "Cooking", "Gaming", "Photography", "Sports", "Music",
        "Art", "Movies", "Technology", "Fitness", "Writing", "Dancing", "Hiking", "Cycling", "Crafting"
    )

    val windowInfo = rememberWindowInfo()

    val dropdownHeight by animateDpAsState(
        targetValue = if (expanded) {
            if (windowInfo.screenWidthInfo == CustomWindowInfo.CustomWindowType.Expanded) {
                // If the window is expanded, show only half the list
                windowInfo.screenHeight / 2
            } else {
                // Otherwise, show the full list
                240.dp
            }
        } else {
            0.dp
        },
        animationSpec = tween(durationMillis = 300), label = ""
    )

    if (expanded) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(dropdownHeight)
                .verticalScroll(rememberScrollState())
                .padding(4.dp)
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onDismissRequest() },
                modifier = Modifier.fillMaxWidth()
            ) {
                interestsList.forEach { interest ->
                    val background = if (interest in selectedInterests) {
                        if (selectedInterests.size > interestsList.size / 2) {
                            Color.Cyan
                        } else {
                            Color.Magenta
                        }
                    } else {
                        Color.White
                    }

                    val updatedInterests = rememberUpdatedState(selectedInterests)

                    DropdownMenuItem(
                        onClick = {
                            val newInterests =
                                if (interest in selectedInterests) {
                                    updatedInterests.value - interest
                                } else {
                                    updatedInterests.value + interest
                                }
                            onInterestsSelected(newInterests)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(background)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = interest in selectedInterests,
                                onCheckedChange = null
                            )
                            Text(interest, color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}