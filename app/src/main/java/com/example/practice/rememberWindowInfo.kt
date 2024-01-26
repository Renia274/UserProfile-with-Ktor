package com.example.practice

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberWindowInfo(): CustomWindowInfo {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    return CustomWindowInfo(
        screenWidthInfo = when {
            configuration.screenWidthDp * density < 600 -> CustomWindowInfo.CustomWindowType.Compact
            configuration.screenWidthDp * density < 840 -> CustomWindowInfo.CustomWindowType.Medium
            else -> CustomWindowInfo.CustomWindowType.Expanded
        },
        screenHeightInfo = when {
            configuration.screenHeightDp * density < 480 -> CustomWindowInfo.CustomWindowType.Compact
            configuration.screenHeightDp * density < 900 -> CustomWindowInfo.CustomWindowType.Medium
            else -> CustomWindowInfo.CustomWindowType.Expanded
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

data class CustomWindowInfo(
    val screenWidthInfo: CustomWindowType,
    val screenHeightInfo: CustomWindowType,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class CustomWindowType {
        data object Compact : CustomWindowType()
        data object Medium : CustomWindowType()
        data object Expanded : CustomWindowType()
    }
}