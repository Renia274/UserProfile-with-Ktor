package com.example.practice.profiles.viewmodel.otp

import androidx.lifecycle.ViewModel
import com.example.practice.services.FirebaseAuthService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FirebaseOtpViewModel @Inject constructor(
    private val firebaseAuthService: FirebaseAuthService
) : ViewModel() {
    private val emailFlow = MutableStateFlow("")


    private val isOtpVerifiedFlow = MutableStateFlow(false)
    val isOtpVerified: StateFlow<Boolean> get() = isOtpVerifiedFlow.asStateFlow()

    private val verificationErrorMessageFlow = MutableStateFlow<String?>(null)
    val verificationErrorMessage: StateFlow<String?> get() = verificationErrorMessageFlow.asStateFlow()

    private val codeSentMessageFlow = MutableStateFlow<String?>(null)
    val codeSentMessage: StateFlow<String?> get() = codeSentMessageFlow.asStateFlow()

    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    fun createOtp(email: String): String {
        this.emailFlow.value = email

        // Your existing code for OTP via email
        // Generate a random 6-digit OTP
        val generatedOtp = (100000..999999).random()

        firebaseAuthService.sendOtpToEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    codeSentMessageFlow.value = "Verification code sent successfully!"
                } else {
                    verificationErrorMessageFlow.value =
                        "Failed to send verification code: ${task.exception?.message}"
                }
            }

        // Return the generated OTP
        return generatedOtp.toString()
    }

    fun verifyOtp(enteredOtp: String) {
        val email = emailFlow.value

        firebaseAuthService.verifyOtpFromEmail(email, enteredOtp)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isOtpVerifiedFlow.value = true
                } else {
                    val errorMessage = "Verification failed: ${task.exception?.message}"
                    verificationErrorMessageFlow.value = errorMessage
                    logToCrashlytics(errorMessage)
                }
            }
    }


    private fun logToCrashlytics(message: String) {
        firebaseCrashlytics.log(message)
        firebaseCrashlytics.recordException(Exception(message))
    }
}