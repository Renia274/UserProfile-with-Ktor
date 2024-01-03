package com.example.practice.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

interface FirebaseAuthService {
    fun signInWithPhoneNumber(phoneNumber: String, callback: PhoneAuthProvider.OnVerificationStateChangedCallbacks)
    fun verifyPhoneNumberWithCode(verificationId: String, code: String): Task<AuthResult>
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Task<AuthResult>



}