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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.practice.CustomWindowInfo
import com.example.practice.datapi.DataLoadingService
import com.example.practice.rememberWindowInfo

@Composable
fun DropDownList(
    onOptionSelected: (String) -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val windowInfo = rememberWindowInfo()

    var selectedProfessions by remember { mutableStateOf(emptyList<String>()) }

    val resources = LocalContext.current.resources


    LaunchedEffect(true) {
        val dataLoadingService = DataLoadingService(resources)
        val dataLoadingApi = dataLoadingService.getDataLoadingApi()
        selectedProfessions = dataLoadingApi.loadProfessions()
    }

    val dropdownHeight by animateDpAsState(
        targetValue = if (expanded) {
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE &&
                windowInfo.screenWidthInfo == CustomWindowInfo.CustomWindowType.Expanded
            ) {
                windowInfo.screenHeight / 2
            } else {
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


