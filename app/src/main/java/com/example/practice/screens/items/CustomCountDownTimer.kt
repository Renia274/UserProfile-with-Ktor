package com.example.practice.screens.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import kotlinx.coroutines.delay


@Composable
fun CustomCountDownTimer(timerViewModel: TimerViewModel = hiltViewModel()) {

    val timerState by timerViewModel.stateFlow.collectAsState()

    LaunchedEffect(key1 = timerState.timeLeft) {
        while (timerViewModel.stateFlow.value.timeLeft > 0 && timerViewModel.stateFlow.value.timerIsRunning) {
            delay(1000L)

            timerViewModel.decreaseTime()
            timerViewModel.stateFlow.value.timeLeft
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Time left: ${timerState.timeLeft}",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.width(90.dp))

        // Reset button
        TextButton(
            onClick = {
                timerViewModel.resetTimer()
                timerViewModel.stateFlow.value.timerIsRunning=true
                timerViewModel.stateFlow.value.timeLeft
                timerViewModel.decreaseTime()
            }
        ) {
            Text(
                text = "Reset",
                fontSize = 16.sp
            )
        }
    }
}
