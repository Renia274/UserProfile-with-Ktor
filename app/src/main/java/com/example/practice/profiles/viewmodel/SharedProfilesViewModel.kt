package com.example.practice.profiles.viewmodel


import androidx.lifecycle.ViewModel
import com.example.practice.data.UserData
import com.example.practice.profiles.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


data class SharedProfilesViewState(
    val savedProfessions: String,
    val signupEmail: String,
    val recoveryEmail: String,
    val darkMode: Boolean,
    val securityEnabled: Boolean,
    val savedInterests: List<String>,

    )

@HiltViewModel
class SharedProfilesViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val userProfilesStateFlow = MutableStateFlow<List<UserData>>(emptyList())
    var userProfiles: StateFlow<List<UserData>> = userProfilesStateFlow
        private set

    private val state = MutableStateFlow(
        SharedProfilesViewState(
            savedProfessions = "",
            signupEmail = "",
            recoveryEmail = "",
            darkMode = false,
            securityEnabled = false,
            savedInterests = emptyList(),

            )
    )

    val stateFlow: StateFlow<SharedProfilesViewState> get() = state


    fun getFirstName(): String {
        return userProfiles.value.firstOrNull()?.firstName ?: ""
    }

    fun getLastName(): String {
        return userProfiles.value.firstOrNull()?.lastName ?: ""
    }


    fun saveProfession(imageResId: Int, profession: String) {
        userRepository.saveProfession(imageResId, profession)
        state.value = state.value.copy(savedProfessions = profession)
    }

    fun setSignupEmail(email: String) {
        state.value = state.value.copy(signupEmail = email)
    }

    fun setRecoveryEmail(email: String) {
        state.value = state.value.copy(recoveryEmail = email)
    }

    fun setDarkMode(isDarkMode: Boolean) {
        state.value = state.value.copy(darkMode = isDarkMode)
    }

    fun setsecurityEnabled(isEnabled: Boolean) {
        state.value = state.value.copy(securityEnabled = isEnabled)
    }

    fun saveInterests(imageResId: Int, interests: List<String>) {
        userRepository.saveInterests(imageResId, interests)
        state.value = state.value.copy(savedInterests = interests)
    }

    // Update userProfiles when needed
    fun updateUserProfiles(newProfiles: List<UserData>) {
        userProfilesStateFlow.value = newProfiles
    }
}
