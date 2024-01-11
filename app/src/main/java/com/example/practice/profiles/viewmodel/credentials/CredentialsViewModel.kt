package com.example.practice.profiles.viewmodel.credentials


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.data.UserCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CredentialsState(
    val enteredCredentials: UserCredentials?,
    val securityCode: String?
)

@HiltViewModel
class CredentialsViewModel @Inject constructor() : ViewModel() {

    private val _credentialsState = MutableStateFlow(CredentialsState(null, null))
    val credentialsState: StateFlow<CredentialsState> get() = _credentialsState

    private val firebaseAuth = FirebaseAuth.getInstance()

    fun setEnteredCredentials(username: String, password: String) {
        val userCredentials = UserCredentials(username, password)
        viewModelScope.launch {
            _credentialsState.value = _credentialsState.value.copy(enteredCredentials = userCredentials)
        }
    }

    fun isValidCredentials(username: String, password: String): Boolean {
        return credentialsState.value.enteredCredentials?.let {
            it.username == username && it.password == password
        } ?: false
    }

    fun updateCredentials(updatedUsername: String, updatedPassword: String) {
        viewModelScope.launch {
            val updatedCredentials = UserCredentials(
                username = updatedUsername,
                password = updatedPassword
            )
            _credentialsState.value = _credentialsState.value.copy(enteredCredentials = updatedCredentials)
        }
    }

    fun saveSecurityCode(code: String): Boolean {
        return try {
            viewModelScope.launch {
                _credentialsState.value = _credentialsState.value.copy(securityCode = code)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun performSignOut() {
        viewModelScope.launch {
            try {
                firebaseAuth.signOut()
            } catch (e: Exception) {
                // Log the error with Crashlytics
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }
}