package com.example.practice.profiles.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practice.data.UserCredentials
import com.example.practice.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() : ViewModel() {

    private val _enteredCredentials = MutableLiveData<UserCredentials>()
    val enteredCredentials: LiveData<UserCredentials>
        get() = _enteredCredentials

    private val _savedProfessions = MutableLiveData<String>()
    val savedProfessions: LiveData<String>
        get() = _savedProfessions

    private val _signupEmail = MutableLiveData<String>()
//    val signupEmail: LiveData<String>
//        get() = _signupEmail

    private val _recoveryEmail = MutableLiveData<String>()
//    val recoveryEmail: LiveData<String>
//        get() = _recoveryEmail

    var userProfiles: List<UserData> = emptyList()

    fun setEnteredCredentials(username: String, password: String) {
        _enteredCredentials.value = UserCredentials(username, password)
    }

    fun saveProfession(imageResId: Int, profession: String) {
        val userProfile = userProfiles.find { it.imageResId == imageResId }
        userProfile?.let {
            it.savedProfession = profession
        }
        _savedProfessions.value = profession
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        val enteredCredentials = _enteredCredentials.value
        return enteredCredentials?.let {
            it.username == username && it.password == password
        } ?: false
    }

    fun setSignupEmail(email: String) {
        _signupEmail.value = email
    }

    fun setRecoveryEmail(email: String) {
        _recoveryEmail.value = email
    }

    fun getEnteredUsername(): String {
        return _enteredCredentials.value?.username.orEmpty()
    }
}
