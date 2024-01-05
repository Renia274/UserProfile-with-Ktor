package com.example.practice.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface FirebaseAuthService {

    // OTP via email
    fun sendOtpToEmail(email: String): Task<Void>
    fun verifyOtpFromEmail(email: String, otp: String): Task<AuthResult>
}