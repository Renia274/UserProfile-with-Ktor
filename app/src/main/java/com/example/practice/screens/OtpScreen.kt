package com.example.practice.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen(onNavigate: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isButtonEnabled by remember { mutableStateOf(true) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val otpViewModel: FirebaseOtpViewModel = hiltViewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Enter Email Address") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrect = false
            ),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = {
                otp = it
                isButtonEnabled = it.length == 6
            },
            label = { Text("Enter OTP") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                autoCorrect = false
            ),
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        )

        Spacer(modifier = Modifier.height(16.dp))



        Button(
            onClick = {
                // Generate and send OTP
                val generatedOtp = otpViewModel.createOtp(email)
                // Automatically fill the OTP field
                otp = generatedOtp
            },
            enabled = email.isNotBlank()
        ) {
            Text("Generate OTP")
        }

        Spacer(modifier = Modifier.height(8.dp))

        otpViewModel.verificationErrorMessage.value?.let { errorMessage ->
            Text(errorMessage, color = Color.Red)
        }

        otpViewModel.codeSentMessage.value?.let { codeSentMessage ->
            Text(codeSentMessage, color = Color.Green)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Verify OTP
                otpViewModel.verifyOtp(otp)
                onNavigate()
                keyboardController?.hide()
            },
            enabled = isButtonEnabled
        ) {
            Text("Verify OTP")
        }


        val isOtpVerified = otpViewModel.isOtpVerified.value
        Text("Is OTP Verified: $isOtpVerified")
    }
}
