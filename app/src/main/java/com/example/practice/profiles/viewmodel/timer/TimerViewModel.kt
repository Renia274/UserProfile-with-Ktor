package com.example.practice.profiles.viewmodel.timer

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.inject.Inject


class TimerViewModel @Inject constructor() : ViewModel() {
    private var _timeLeft = mutableStateOf(60)
    var timeLeft: State<Int> = _timeLeft
        private set

    var timerIsRunning by mutableStateOf(true)

    fun decreaseTime() {
        _timeLeft.value--
    }

    fun resetTimer() {
        _timeLeft.value = 60
    }


}