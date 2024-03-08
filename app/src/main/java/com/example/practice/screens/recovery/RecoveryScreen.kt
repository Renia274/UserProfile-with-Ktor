package com.example.practice.screens.recovery

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.ui.theme.PracticeTheme


@Composable
fun RecoveryScreen(
    navigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SharedProfilesViewModel
) {
    val context = LocalContext.current

    val sharedState by viewModel.stateFlow.collectAsState()
    val signupEmail by remember { mutableStateOf(sharedState.signupEmail) }

    RecoveryScreenContent(
        navigateToLogin = navigateToLogin,
        onNavigateBack = onNavigateBack,
        signupEmail = signupEmail
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoveryScreenContent(
    navigateToLogin: () -> Unit,
    onNavigateBack: () -> Unit,
    signupEmail: String
) {
    val email = remember { mutableStateOf("") }
    val isRecoveryEmailSent = remember { mutableStateOf(false) }
    val showError = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopAppBar(
            title = { Text(text = "Password Recovery", fontFamily = FontFamily.SansSerif) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Yellow)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Welcome to the Password Recovery Screen!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Enter your email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (email.value == signupEmail) {
//                        onOpenRecoveryLink()
                        isRecoveryEmailSent.value = true
                    } else {
                        showError.value = true
                    }
                }
            ) {
                Text("Reset")
            }
        }

        // Display error message if showError is true
        if (showError.value) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Entered email doesn't match the signup email.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // Display message if recovery email is sent
        if (isRecoveryEmailSent.value) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Recovery email sent to ${email.value}. Please check your email.",
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            navigateToLogin()
        }
    }
}




@Preview(showBackground = true)
@Composable
fun RecoveryScreenContentPreview() {
    val navigateToLogin: () -> Unit = {}
    val onNavigateBack: () -> Unit = {}
    val signupEmail = "example@example.com"

    PracticeTheme {
        Surface {
            RecoveryScreenContent(
                navigateToLogin = navigateToLogin,
                onNavigateBack = onNavigateBack,
                signupEmail = signupEmail
            )
        }
    }
}

