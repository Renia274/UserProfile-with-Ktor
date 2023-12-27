package com.example.practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.practice.data.CustomWindowInfo
import com.example.practice.data.rememberWindowInfo
import com.example.practice.navigation.graph.NavigationApp
import com.example.practice.ui.theme.PracticeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            PracticeTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                ) {
                    val windowInfo = rememberWindowInfo()

                    DisplayWindowInfo(windowInfo)
                    NavigationApp()
                }
            }
        }
    }

    @Composable
    private fun DisplayWindowInfo(windowInfo: CustomWindowInfo) {
        when (windowInfo.screenWidthInfo) {
            is CustomWindowInfo.CustomWindowType.Compact -> {
                Text("Compact Window - ${windowInfo.screenWidth}")
                BasicTextField(value = "Compact Window", onValueChange = {})
            }
            is CustomWindowInfo.CustomWindowType.Medium -> {
                Text("Medium Window - ${windowInfo.screenWidth}")
            }
            is CustomWindowInfo.CustomWindowType.Expanded -> {
                Text("Expanded Window - ${windowInfo.screenWidth}")
            }
        }
    }
}
