package com.example.practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import com.example.practice.navigation.graph.NavigationApp
import com.example.practice.ui.theme.PracticeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            PracticeTheme {
                Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                ) {
                    NavigationApp()

                }
            }
        }
    }
}



