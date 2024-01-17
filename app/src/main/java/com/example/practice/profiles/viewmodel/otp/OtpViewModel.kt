package com.example.practice.profiles.viewmodel.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice.services.FirebaseAuthService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FirebaseOtpViewState(
    val isOtpVerified: Boolean = false,
    val verificationErrorMessage: String? = null,
    val codeSentMessage: String? = null,
    val emailErrorMessage: String? = null,
)

@HiltViewModel
class FirebaseOtpViewModel @Inject constructor(
    private val firebaseAuthService: FirebaseAuthService
) : ViewModel() {
    private val emailFlow = MutableStateFlow("")

    val viewStateFlow = MutableStateFlow(FirebaseOtpViewState())

    val viewState: StateFlow<FirebaseOtpViewState> get() = viewStateFlow.asStateFlow()

    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    fun createOtp(email: String, signupEmail: String): String {
        emailFlow.value = email

        // Generate a random 6-digit OTP
        val generatedOtp = (100000..999999).random()

        if (viewState.value.emailErrorMessage == null) {
            // Proceed with sending OTP only if the email matches the signup email
            firebaseAuthService.sendOtpToEmail(email)
                .addOnCompleteListener { task ->
                    viewModelScope.launch {
                        if (task.isSuccessful) {
                            if (email.isNotBlank() && email == signupEmail) {
                                viewStateFlow.value = viewStateFlow.value.copy(
                                    codeSentMessage = "OTP created successfully, please verify the code"
                                )
                            }
                        } else {
                            // Handle the case where OTP sending fails
                            viewStateFlow.value = viewStateFlow.value.copy(
                                verificationErrorMessage = "Failed to send verification code: ${task.exception?.message}"
                            )
                        }
                    }
                }
        }

        // Return the generated OTP
        return generatedOtp.toString()
    }

    fun verifyOtp(enteredOtp: String) {
        val email = emailFlow.value

        firebaseAuthService.verifyOtpFromEmail(email, enteredOtp)
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    if (task.isSuccessful) {
                        viewStateFlow.value = viewStateFlow.value.copy(isOtpVerified = true)
                    } else {
                        val errorMessage = "Verification failed: ${task.exception?.message}"
                        viewStateFlow.value = viewStateFlow.value.copy(verificationErrorMessage = errorMessage)

                    }
                }
            }
    }

    fun clearEmailErrorMessage() {
        viewStateFlow.value = viewStateFlow.value.copy(emailErrorMessage = null)
    }

    fun clearCodeMessage() {
        viewStateFlow.value = viewStateFlow.value.copy(codeSentMessage = null)
    }



    // set error message for non-matching email
    fun setErrorEmail(email: String, signupEmail: String) {
        val emailErrorMessage = createEmailErrorMessage(email, signupEmail)
        viewStateFlow.value = viewStateFlow.value.copy(emailErrorMessage = emailErrorMessage)
    }

    private fun createEmailErrorMessage(email: String, signupEmail: String): String? {
        return if (email.isNotBlank() && email != signupEmail) {
            "Entered email doesn't match the signup email"
        } else {
            null
        }
    }
    fun logToCrashlytics(message: String) {
        firebaseCrashlytics.log(message)
        firebaseCrashlytics.recordException(Exception(message))
    }
}