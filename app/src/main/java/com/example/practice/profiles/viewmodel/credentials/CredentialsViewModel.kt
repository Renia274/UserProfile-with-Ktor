package com.example.practice.profiles.viewmodel.credentials


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.data.UserCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialsViewModel @Inject constructor() : ViewModel() {

    private val _enteredCredentials = MutableLiveData<UserCredentials>()
    val enteredCredentials: LiveData<UserCredentials>
        get() = _enteredCredentials


    private val _securityCode = MutableLiveData<String>()
    val securityCode: LiveData<String>
        get() = _securityCode

    fun setEnteredCredentials(username: String, password: String) {
        _enteredCredentials.value = UserCredentials(username, password)
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        val enteredCredentials = _enteredCredentials.value
        return enteredCredentials?.let {
            it.username == username && it.password == password
        } ?: false
    }


    fun updateCredentials(updatedUsername: String, updatedPassword: String) {
        viewModelScope.launch {
            // Get the current value of _enteredCredentials
            val currentCredentials = _enteredCredentials.value

            // Update only if the currentCredentials is not null
            if (currentCredentials != null) {
                // Create a new UserCredentials object with updated values
                val updatedCredentials = UserCredentials(
                    username = updatedUsername,
                    password = updatedPassword
                )

                // Set the updated credentials to the LiveData
                _enteredCredentials.value = updatedCredentials
            }
        }
    }

    fun saveSecurityCode(code: String): Boolean {
        return try {
            viewModelScope.launch {
                _securityCode.value = code
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}