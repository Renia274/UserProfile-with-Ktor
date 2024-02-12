package com.example.practice.screens.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.logs.app.AppLogger
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewModel
import com.example.practice.ui.theme.PracticeTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen(
    onNavigate: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: SharedProfilesViewModel,
    otpViewModel: FirebaseOtpViewModel
) {
    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current

    val sharedState by viewModel.stateFlow.collectAsState()
    var signupEmail by remember { mutableStateOf(sharedState.signupEmail) }

    val viewState by otpViewModel.viewStateFlow.collectAsState()
    var emailErrorMessage = viewState.emailErrorMessage
    val codeMessage = otpViewModel.viewStateFlow.value.codeSentMessage
    var showMessage by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            // Reset the codeMessage and errorMessage when navigating away from the OTP screen
            otpViewModel.clearCodeMessage()
            otpViewModel.clearEmailErrorMessage()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        TopAppBar(title = { Text("OTP") }, navigationIcon = {
            IconButton(onClick = { onBackPressed() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })

        Spacer(modifier = Modifier.height(16.dp))

        OtpScreenContent(
            email = email,
            onEmailChange = { email = it; otpViewModel.clearEmailErrorMessage() },
            otp = otp,
            onOtpChange = { otp = it },
            onGenerateClick = {
                if (email == signupEmail) {
                    otpViewModel.clearEmailErrorMessage()
                    val generatedOtp = otpViewModel.createOtp(email, signupEmail)
                    otp = generatedOtp
                    showMessage = true
                    // Log OTP generation event
                    AppLogger.logEvent("otp_generation")
                } else {
                    if (signupEmail != null) {
                        otpViewModel.setErrorEmail(email, signupEmail)
                    }
                    showMessage = false
                    // Log unsuccessful OTP generation event
                    AppLogger.logError("Unsuccessful OTP generation attempt: Email does not match signup email")
                }
            },
            onVerifyClick = {
                if (email == signupEmail) {
                    otpViewModel.verifyOtp(otp)
                    onNavigate()
                    keyboardController?.hide()
                    // Log successful OTP verification event
                    AppLogger.logEvent("otp_verification")
                } else {
                    showMessage = true
                    if (signupEmail != null) {
                        otpViewModel.setErrorEmail(email, signupEmail)
                    }
                    emailErrorMessage = "Please enter the email used during signup"
                    // Log unsuccessful OTP verification event
                    AppLogger.logError("Unsuccessful OTP verification attempt: Email does not match signup email")
                }
            },
            emailErrorMessage = emailErrorMessage,
            codeMessage = codeMessage,
            showMessage = showMessage,
            signupEmail = signupEmail
        )
    }
}

@Composable
fun OtpScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    otp: String,
    onOtpChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    onVerifyClick: () -> Unit,
    emailErrorMessage: String?,
    codeMessage: String?,
    showMessage: Boolean,
    signupEmail: String
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { onEmailChange(it) },
                label = { Text("Enter Email Address") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, autoCorrect = false
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (email.isNotBlank() && email != signupEmail) Color.Red else Color.Transparent
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = otp,
                onValueChange = { onOtpChange(it) },
                label = { Text("Enter OTP") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, autoCorrect = false
                ),
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onGenerateClick() },
            enabled = email.isNotBlank(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Generate OTP")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onVerifyClick() },
            enabled = email.isNotBlank(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Verify OTP")
        }

        // Display code message when the user creates OTP code with signup email
        Spacer(modifier = Modifier.height(8.dp))
        if (codeMessage != null) {
            Text(
                codeMessage,
                color = Color.Green,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        // Display code error message when the user tries to create OTP code with any email
        if (emailErrorMessage != null) {
            Text(
                emailErrorMessage,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OtpScreenPreview() {

    PracticeTheme {
        OtpScreenContent(
            email = "example@email.com",
            onEmailChange = {},
            otp = "123456",
            onOtpChange = {},
            onGenerateClick = {},
            onVerifyClick = {},
            emailErrorMessage = "Invalid email",
            codeMessage = "OTP sent successfully",
            showMessage = true,
            signupEmail = "test@gmail.com"
        )
    }


}