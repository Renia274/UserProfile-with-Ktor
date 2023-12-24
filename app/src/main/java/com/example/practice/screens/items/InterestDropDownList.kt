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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InterestsDropDownList(
    onInterestsSelected: (List<String>) -> Unit,
    selectedInterests: List<String>,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    // List of interests/hobbies
    val interestsList = listOf(
        "Reading",
        "Traveling",
        "Cooking",
        "Gaming",
        "Photography",
        "Sports",
        "Music",
        "Art",
        "Movies",
        "Technology",
        "Fitness",
        "Writing",
        "Dancing",
        "Hiking",
        "Cycling",
        "Crafting"
    )

    val dropdownHeight by animateDpAsState(
        targetValue = if (expanded) 240.dp else 0.dp,
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
                modifier = Modifier.fillMaxWidth().background(Color.Green)
            ) {
                interestsList.forEach { interest ->
                    DropdownMenuItem(
                        onClick = {
                            val updatedInterests =
                                if (selectedInterests.contains(interest)) {
                                    selectedInterests - interest
                                } else {
                                    selectedInterests + interest
                                }
                            onInterestsSelected(updatedInterests)
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = selectedInterests.contains(interest),
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