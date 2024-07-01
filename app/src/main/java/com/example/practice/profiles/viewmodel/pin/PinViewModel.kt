package com.example.practice.profiles.viewmodel.pin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class PinState(
    var pin: String = ""
)



@HiltViewModel
class PinViewModel @Inject constructor() : ViewModel() {
    private val userPins: MutableMap<String, String> = mutableMapOf()
    private val pinState: PinState = PinState()

    fun savePinForProfile(username: String, pin: String) {
        userPins[username] = pin
        pinState.pin = pin
    }

    fun getPinForProfile(username: String): String? {
        return userPins[username]
    }
}
