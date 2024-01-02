package com.example.practice.profiles.viewmodel.credentials


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.data.UserCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialsViewModel @Inject constructor() : ViewModel() {

    val enteredCredentials = MutableStateFlow<UserCredentials?>(null)
    private val enteredCredentialsFlow: StateFlow<UserCredentials?> = enteredCredentials

    val securityCode = MutableStateFlow<String?>(null)


    fun setEnteredCredentials(username: String, password: String) {
        val userCredentials = UserCredentials(username, password)
        viewModelScope.launch {
            enteredCredentials.value = userCredentials
        }
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        return enteredCredentialsFlow.value?.let {
            it.username == username && it.password == password
        } ?: false
    }

    fun updateCredentials(updatedUsername: String, updatedPassword: String) {
        viewModelScope.launch {
            val updatedCredentials = UserCredentials(
                username = updatedUsername,
                password = updatedPassword
            )
            enteredCredentials.value = updatedCredentials
        }
    }

    fun saveSecurityCode(code: String): Boolean {
        return try {
            viewModelScope.launch {
                securityCode.value = code
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}