package com.example.practice.profiles.viewmodel



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practice.data.UserData
import com.example.practice.profiles.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedProfilesViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _savedProfessions = MutableLiveData<String>()
    val savedProfessions: LiveData<String>
        get() = _savedProfessions

    private val _signupEmail = MutableLiveData<String>()
    val signupEmail: LiveData<String>
        get() = _signupEmail

    private val _recoveryEmail = MutableLiveData<String>()
    val recoveryEmail: LiveData<String>
        get() = _recoveryEmail

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean>
        get() = _darkMode

    private val _notificationEnabled = MutableLiveData<Boolean>()
    val notificationEnabled: LiveData<Boolean>
        get() = _notificationEnabled

    var userProfiles: List<UserData> = emptyList()

    private val _savedInterests = MutableLiveData<List<String>>()
    val savedInterests: LiveData<List<String>>
        get() = _savedInterests

    private val _interestsDropdownVisible = MutableLiveData<Boolean>()
    val interestsDropdownVisible: LiveData<Boolean>
        get() = _interestsDropdownVisible

    fun saveProfession(imageResId: Int, profession: String) {
        userRepository.saveProfession(imageResId, profession)
        _savedProfessions.value = profession
    }

    fun setSignupEmail(email: String) {
        _signupEmail.value = email
    }

    fun setRecoveryEmail(email: String) {
        _recoveryEmail.value = email
    }

    fun setDarkMode(isDarkMode: Boolean) {
        _darkMode.value = isDarkMode
    }

    fun setNotificationEnabled(isEnabled: Boolean) {
        _notificationEnabled.value = isEnabled
    }

    fun saveInterests(imageResId: Int, interests: List<String>) {
        userRepository.saveInterests(imageResId, interests)
        _savedInterests.value = interests
    }




}