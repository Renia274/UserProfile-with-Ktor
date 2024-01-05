package com.example.practice.profiles.viewmodel.credentials


import android.app.AlertDialog
import android.content.Context
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

@HiltViewModel
class CredentialsViewModel @Inject constructor() : ViewModel() {

    val enteredCredentials = MutableStateFlow<UserCredentials?>(null)
    private val enteredCredentialsFlow: StateFlow<UserCredentials?> = enteredCredentials

    val securityCode = MutableStateFlow<String?>(null)

    private val firebaseAuth = FirebaseAuth.getInstance()

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


    fun signOut(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Sign Out")
            .setMessage("Are you sure you want to sign out?")
            .setPositiveButton("Sign Out") { _, _ ->
                performSignOut()
            }
            .setNegativeButton("Cancel", null)
            .create()

        alertDialog.show()
    }

    private fun performSignOut() {
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