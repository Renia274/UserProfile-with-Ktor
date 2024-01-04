package com.example.practice.services



import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject


class FirebaseAuthServiceImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : FirebaseAuthService {


    // For OTP via email
    override fun sendOtpToEmail(email: String): Task<Void> {
        // Generate a random 6-digit OTP
        val otpCode = (100000..999999).random()

        // Print the OTP for testing purposes
        println("OTP for $email: $otpCode")


        // For now, just return a successful Task
        return Tasks.forResult(null)
    }

    override fun verifyOtpFromEmail(email: String, otp: String): Task<AuthResult> {
        // Build the email address by appending a placeholder domain
        val emailWithDomain = "$email@yourdomain.com"

        // Sign in with the provided email and OTP
        return firebaseAuth.signInWithEmailAndPassword(emailWithDomain, otp)
            .continueWithTask { signInTask ->
                // Check if the sign-in task was successful
                if (signInTask.isSuccessful) {
                    // If successful, return the AuthResult
                    Tasks.forResult(signInTask.result)
                } else {
                    // If unsuccessful, return an error Task with the exception
                    Tasks.forException(signInTask.exception!!)
                }
            }
    }

}