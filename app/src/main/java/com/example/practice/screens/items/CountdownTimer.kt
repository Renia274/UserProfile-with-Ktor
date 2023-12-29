package com.example.practice.screens.items

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import kotlinx.coroutines.delay


@Composable
fun CountdownTimer(timerViewModel: TimerViewModel = hiltViewModel()) {

    var timeLeftState by remember { mutableStateOf(timerViewModel.timeLeft.value) }
    var isTimerRunning by remember { mutableStateOf(timerViewModel.timerIsRunning) }



    LaunchedEffect(key1 = timeLeftState) {
        while (timeLeftState > 0 && isTimerRunning) {
            delay(1000L)

            timeLeftState = timerViewModel.timeLeft.value
            // Decrease timeLeft each second
            timerViewModel.decreaseTime()
            // Update the local timeLeft value
            timeLeftState = timerViewModel.timeLeft.value
        }

    }




    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Time left: $timeLeftState",
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.width(90.dp))


        // Reset button
        TextButton(
            onClick = {
                // Reset the timer to its initial value (60 seconds)
                timerViewModel.resetTimer()
                timerViewModel.timerIsRunning = true

                // Update the local timeLeft value
                timeLeftState = timerViewModel.timeLeft.value

                // Decrease timeLeft after updating the local value
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

