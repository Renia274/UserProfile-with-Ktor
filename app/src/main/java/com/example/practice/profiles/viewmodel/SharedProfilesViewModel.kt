package com.example.practice.profiles.viewmodel


import androidx.lifecycle.ViewModel
import com.example.practice.data.UserData
import com.example.practice.profiles.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class SharedProfilesViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val savedProfessions = MutableStateFlow("")

    private val _signupEmail = MutableStateFlow("")
    val signupEmail: StateFlow<String> get() = _signupEmail

    private val recoveryEmail = MutableStateFlow("")

    var darkMode = MutableStateFlow(false)

    val notificationEnabled = MutableStateFlow(false)

    private val savedInterests = MutableStateFlow<List<String>>(emptyList())


    var userProfiles: List<UserData> = emptyList()

    fun saveProfession(imageResId: Int, profession: String) {
        userRepository.saveProfession(imageResId, profession)
        savedProfessions.value = profession
    }

    fun setSignupEmail(email: String) {
        _signupEmail.value = email
    }

    fun setRecoveryEmail(email: String) {
        recoveryEmail.value = email
    }

    fun setDarkMode(isDarkMode: Boolean) {
        darkMode.value = isDarkMode
    }

    fun setNotificationEnabled(isEnabled: Boolean) {
        notificationEnabled.value = isEnabled
    }

    fun saveInterests(imageResId: Int, interests: List<String>) {
        userRepository.saveInterests(imageResId, interests)
        savedInterests.value = interests
    }




}
