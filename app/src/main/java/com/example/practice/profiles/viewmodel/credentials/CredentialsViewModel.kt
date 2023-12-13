package com.example.practice.profiles.viewmodel.credentials


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practice.data.UserCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CredentialsViewModel @Inject constructor() : ViewModel() {

    private val _enteredCredentials = MutableLiveData<UserCredentials>()
    val enteredCredentials: LiveData<UserCredentials>
        get() = _enteredCredentials

    fun setEnteredCredentials(username: String, password: String) {
        _enteredCredentials.value = UserCredentials(username, password)
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        val enteredCredentials = _enteredCredentials.value
        return enteredCredentials?.let {
            it.username == username && it.password == password
        } ?: false
    }

    fun getEnteredUsername(): String {
        return _enteredCredentials.value?.username.orEmpty()
    }
}