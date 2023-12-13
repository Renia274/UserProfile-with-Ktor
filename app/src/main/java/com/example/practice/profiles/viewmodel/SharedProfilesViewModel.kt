package com.example.practice.profiles.viewmodel



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practice.data.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedProfilesViewModel @Inject constructor() : ViewModel() {

    private val _savedProfessions = MutableLiveData<String>()
    val savedProfessions: LiveData<String>
        get() = _savedProfessions

    private val _signupEmail = MutableLiveData<String>()
    val signupEmail: LiveData<String>
        get() = _signupEmail

    private val _recoveryEmail = MutableLiveData<String>()
    val recoveryEmail: LiveData<String>
        get() = _recoveryEmail

    var userProfiles: List<UserData> = emptyList()

    fun saveProfession(imageResId: Int, profession: String) {
        val userProfile = userProfiles.find { it.imageResId == imageResId }
        userProfile?.let {
            it.savedProfession = profession
        }
        _savedProfessions.value = profession
    }

    fun setSignupEmail(email: String) {
        _signupEmail.value = email
    }

    fun setRecoveryEmail(email: String) {
        _recoveryEmail.value = email
    }
}