package com.example.practice.screens.splash.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.delay

const val SplashWaitTimeMillis = 2000L
const val AnimationDurationMillis = 1000

@Composable
fun SplashScreen(onLoadingComplete: () -> Unit) {
    var isLoading by mutableStateOf(true)

    LaunchedEffect(isLoading) {
        delay(SplashWaitTimeMillis)
        isLoading = false
        onLoadingComplete.invoke()
    }

    val transitionState = mutableStateOf(0)
    val alpha by animateFloatAsState(
        targetValue = if (transitionState.value == 0) 1f else 0f,
        animationSpec = tween(durationMillis = AnimationDurationMillis),
        label = "My animated splash screen"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            // Image with fading transition
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {
                SplashScreenContent(alpha = alpha)
            }
        }
    }
}

@Composable
fun SplashScreenContent(alpha: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = null,
            modifier = Modifier.alpha(alpha)
        )

        Text(
            text = "Welcome!",
            color = Color.Black,
            fontSize = 24.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenContentPreview() {
    PracticeTheme {
        SplashScreenContent(alpha = 1f)
    }


}
