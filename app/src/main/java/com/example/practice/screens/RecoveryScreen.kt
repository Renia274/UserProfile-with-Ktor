package com.example.practice.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import kotlinx.coroutines.delay

@Composable
fun RecoveryScreen(
    navigateToLogin: () -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {

    val overrideFontPadding = PlatformTextStyle(includeFontPadding = false)
    val h4 = TextStyle(
        fontSize = 16.sp,
        platformStyle = overrideFontPadding
    )

    var email by remember { mutableStateOf("") }
    var isRecoveryEmailSent by remember { mutableStateOf(false) }
    var isDelayComplete by remember { mutableStateOf(false) }

    LaunchedEffect(isRecoveryEmailSent) {
        delay(8000)
        isRecoveryEmailSent = false
        isDelayComplete = true
        delay(8000)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Password Recovery",
            style = h4,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter your email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isRecoveryEmailSent = true
                viewModel.setSignupEmail(email)
                navigateToLogin.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Recover Password")
        }

        if (isRecoveryEmailSent) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Recovery email sent to $email. Please check your email.",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary
            )

            if (isDelayComplete) {
                isRecoveryEmailSent = true
                viewModel.setRecoveryEmail(email)
                navigateToLogin.invoke()
            }
        }
    }
}
