package com.example.practice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.practice.navigation.graph.NavigationApp
import com.example.practice.ui.theme.PracticeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavigationApp()
                }
            }
        }
    }


}
