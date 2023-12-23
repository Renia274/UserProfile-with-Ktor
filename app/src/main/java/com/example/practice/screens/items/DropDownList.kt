package com.example.practice.screens.items

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropDownList(
    onOptionSelected: (String) -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    // List of professions
    val selectedProfessions = listOf(
        "Software Engineer",
        "Data Scientist",
        "Product Manager",
        "UX Designer",
        "Network Engineer",
        "Security Analyst",
        "Database Administrator",
        "AI Researcher",
        "Game Developer",
        "Web Developer",
        "DevOps Engineer",
        "IT Consultant",
        "UI/UX Designer",
        "System Administrator",
        "Mobile App Developer",
        "Quality Assurance Engineer",
        "Network Architect",
        "Business Analyst",
        "Technical Writer",
        "Cloud Solutions Architect"
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
                selectedProfessions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            onDismissRequest()
                        }
                    ) {
                        Text(option, color = Color.Black)
                    }
                }
            }
        }
    }
}