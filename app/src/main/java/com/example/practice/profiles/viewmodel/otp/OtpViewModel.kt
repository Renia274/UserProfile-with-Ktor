package com.example.practice.profiles.viewmodel.otp

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.practice.profiles.repository.UserRepository
import com.example.practice.services.FirebaseAuthService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class FirebaseOtpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseAuthService: FirebaseAuthService,
    userRepository: UserRepository
) : ViewModel() {
    private val verificationIdFlow = MutableStateFlow(savedStateHandle.get<String>("verificationId") ?: "")
    val verificationId: StateFlow<String> get() = verificationIdFlow.asStateFlow()

    private val otpFlow = MutableStateFlow<String?>(null)
    val otp: StateFlow<String?> get() = otpFlow.asStateFlow()

    private val isOtpVerifiedFlow = MutableStateFlow(false)
    val isOtpVerified: StateFlow<Boolean> get() = isOtpVerifiedFlow.asStateFlow()

    private val verificationErrorMessageFlow = MutableStateFlow<String?>(null)
    val verificationErrorMessage: StateFlow<String?> get() = verificationErrorMessageFlow.asStateFlow()

    private val codeSentMessageFlow = MutableStateFlow<String?>(null)
    val codeSentMessage: StateFlow<String?> get() = codeSentMessageFlow.asStateFlow()

    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()


    private val isUserAuthenticatedFlow = MutableStateFlow(userRepository.isUserAuthenticated())
    val isUserAuthenticated: StateFlow<Boolean> get() = isUserAuthenticatedFlow.asStateFlow()

    fun sendOtp(phoneNumber: String, activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    verificationErrorMessageFlow.value = "Verification failed: ${e.message}"
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    verificationIdFlow.value = verificationId
                    codeSentMessageFlow.value = "Verification code sent successfully!"
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(enteredOtp: String) {
        // Check if the user is authenticated before proceeding with OTP verification
        if (isUserAuthenticatedFlow.value) {
            // Your existing code for OTP verification
            val verificationId = verificationIdFlow.value
            val credential = PhoneAuthProvider.getCredential(verificationId, enteredOtp)

            firebaseAuthService.signInWithPhoneAuthCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isOtpVerifiedFlow.value = true
                    } else {
                        // Handle verification failure
                        logToCrashlytics("Verification failed: ${task.exception?.message}")
                    }
                }
        } else {
            isUserAuthenticatedFlow.value = false
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    isOtpVerifiedFlow.value = true
                } else {
                    // Handle sign-in failure
                    logToCrashlytics("Sign-in failed: ${task.exception?.message}")
                }
            }
    }

    private fun logToCrashlytics(message: String) {
        firebaseCrashlytics.log(message)
        firebaseCrashlytics.recordException(Exception(message))
    }
}