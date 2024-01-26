package com.example.practice.screens.userprofile.editprofile.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.practice.CustomWindowInfo
import com.example.practice.rememberWindowInfo

@Composable
fun DropDownList(
    onOptionSelected: (String) -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val windowInfo = rememberWindowInfo()

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
        targetValue = if (expanded) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                windowInfo.screenWidthInfo == CustomWindowInfo.CustomWindowType.Expanded
            ) {
                // If landscape and the window is expanded, show only half the list
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
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onDismissRequest() },
                modifier = Modifier.fillMaxWidth()
            ) {
                selectedProfessions.forEachIndexed { index, option ->
                    val itemModifier = Modifier
                        .fillMaxWidth()
                        .background(
                            when {
                                index <= 8 -> Color.Yellow
                                else -> Color.Magenta
                            }
                        )

                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            onDismissRequest()
                        },
                        modifier = itemModifier
                    ) {
                        Text(option, color = Color.Black)
                    }
                }
            }
        }
    }
}


