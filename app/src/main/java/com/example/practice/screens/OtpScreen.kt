package com.example.practice.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpScreen(
    onNavigate: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel(),
    otpViewModel: FirebaseOtpViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var showMessage by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val signupEmail by viewModel.signupEmail.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // TopAppBar
        TopAppBar(
            title = { Text("OTP") },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // email
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
                .border(
                    width = 1.dp,
                    color = if (email.isNotBlank() && email != signupEmail) Color.Red else Color.Transparent
                )
        )

        Spacer(modifier = Modifier.height(4.dp))


        //  OTP
        OutlinedTextField(
            value = otp,
            onValueChange = {
                otp = it
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
                val generatedOtp = otpViewModel.createOtp(email, signupEmail)
                // Automatically fill the OTP field
                otp = generatedOtp
                showMessage = true
            },
            enabled = email.isNotBlank(),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Generate OTP")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display codeSentMessage if email matches signupEmail
        if (showMessage) {
            Spacer(modifier = Modifier.height(16.dp))

            // Button to verify OTP
            Button(
                onClick = {
                    // Verify OTP only if the entered email matches the signup email
                    if (email == signupEmail) {
                        otpViewModel.verifyOtp(otp)
                        onNavigate()
                        keyboardController?.hide()
                    } else {
                        // Show an error message if the entered email is different from the signup email
                        showMessage = false
                        Toast.makeText(
                            context,
                            "Entered email doesn't match the signup email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                enabled = email == signupEmail,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Verify OTP")
            }

            otpViewModel.codeSentMessage.value?.let { codeSentMessage ->
                Text(
                    codeSentMessage,
                    color = Color.Green,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
