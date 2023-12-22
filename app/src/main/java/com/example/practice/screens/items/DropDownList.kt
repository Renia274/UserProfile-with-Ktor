package com.example.practice.screens.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
        "IT Consultant"
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.fillMaxWidth()
    ) {
        selectedProfessions.forEach { option ->
            DropdownMenuItem(
                onClick = {
                    onOptionSelected(option)
                    onDismissRequest()
                }
            ) {
                Text(option)
            }
        }
    }
}
