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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreen(
    navigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    val signupEmail by viewModel.signupEmail.collectAsState()

    val overrideFontPadding = PlatformTextStyle(includeFontPadding = false)
    val h4 = TextStyle(
        fontSize = 16.sp,
        platformStyle = overrideFontPadding
    )

    var email by remember { mutableStateOf("") }
    var isRecoveryEmailSent by remember { mutableStateOf(false) }
    var isDelayComplete by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    LaunchedEffect(isRecoveryEmailSent) {
        delay(8000)
        isRecoveryEmailSent = false
        isDelayComplete = true
        delay(8000)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // Top App Bar with back arrow
        TopAppBar(
            title = { Text(text = "Password Recovery", style = h4, textAlign = TextAlign.Center) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Yellow)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
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
                    if (email == signupEmail) {
                        isRecoveryEmailSent = true
                        viewModel.setSignupEmail(email)
                        navigateToLogin.invoke()
                    } else {
                        // Show an error message If you put different mail that the signup mail
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Recover Password")
            }

            if (showError) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Entered email doesn't match the signup email.",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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
}