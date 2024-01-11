package com.example.practice.profiles.viewmodel.timer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class TimerViewState(
    val timeLeft: Int,
    var timerIsRunning: Boolean
)
@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    private val timerStateFlow = MutableStateFlow(TimerViewState(timeLeft = 60, timerIsRunning = true))
    val stateFlow = timerStateFlow.asStateFlow()

    fun decreaseTime() {
        val currentState = timerStateFlow.value
        timerStateFlow.value = currentState.copy(timeLeft = currentState.timeLeft - 1)
    }

    fun resetTimer() {
        timerStateFlow.value = TimerViewState(timeLeft = 60, timerIsRunning = true)
    }
}