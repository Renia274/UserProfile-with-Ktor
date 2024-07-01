package com.example.practice.profiles.viewmodel.pin


import android.preference.PreferenceManager
import androidx.lifecycle.ViewModel
import com.example.practice.MyApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


data class PinState(
    val pin: String?
)

@HiltViewModel
class PinViewModel @Inject constructor(
    private val application: MyApplication
) : ViewModel() {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    fun savePinForProfile(profileName: String, pin: String) {
        sharedPreferences.edit().putString(getPinKeyForProfile(profileName), pin).apply()
    }

    fun getSavedPinForProfile(profileName: String): String? {
        return sharedPreferences.getString(getPinKeyForProfile(profileName), null)
    }

    fun matchPinToProfile(pin: String): String? {
        val profiles = listOf("Bob", "Alice", "Eve")
        for (profile in profiles) {
            val savedPin = getSavedPinForProfile(profile)
            if (pin == savedPin) {
                return profile
            }
        }
        return null
    }

    private fun getPinKeyForProfile(profileName: String): String {
        return "$PIN_KEY.$profileName"
    }

    companion object {
        private const val PIN_KEY = "pin_key"
    }
}