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

    private val verificationErrorMessageFlow = MutableStateFlow<String?>(null)

    private val codeSentMessageFlow = MutableStateFlow<String?>(null)
    val codeSentMessage: StateFlow<String?> get() = codeSentMessageFlow.asStateFlow()

    val emailErrorMessageFlow = MutableStateFlow<String?>(null)
    val emailErrorMessage: StateFlow<String?> get() = emailErrorMessageFlow.asStateFlow()


    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()



    fun createOtp(email: String, signupEmail: String): String {
        this.emailFlow.value = email

        // Generate a random 6-digit OTP
        val generatedOtp = (100000..999999).random()

        if (emailErrorMessageFlow.value == null) {
            // Proceed with sending OTP only if the email matches the signup email
            firebaseAuthService.sendOtpToEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (email.isNotBlank() && email == signupEmail) {
                            codeSentMessageFlow.value = "OTP created successfully, please verify the code"
                        }
                    } else {
                        // Handle the case where OTP sending fails
                        verificationErrorMessageFlow.value =
                            "Failed to send verification code: ${task.exception?.message}"
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
                if (task.isSuccessful) {
                    isOtpVerifiedFlow.value = true
                } else {
                    val errorMessage = "Verification failed: ${task.exception?.message}"
                    verificationErrorMessageFlow.value = errorMessage
                    logToCrashlytics(errorMessage)
                }
            }
    }



    fun clearEmailErrorMessage() {
        emailErrorMessageFlow.value = null
    }


    // Function to set error message for non-matching email
    fun setErrorEmail(email: String, signupEmail: String) {
        val emailErrorMessage = createEmailErrorMessage(email, signupEmail)
        emailErrorMessageFlow.value = emailErrorMessage
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