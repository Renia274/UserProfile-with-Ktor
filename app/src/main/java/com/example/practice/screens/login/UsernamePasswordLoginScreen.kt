package com.example.practice.screens.login

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.logs.app.AppLogger
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.services.FirebaseAnalyticsService
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.launch


@Composable
fun UsernamePasswordLoginScreen(
    onLoginSuccess: (String, String, String, String) -> Unit,
    onLoading: (Boolean) -> Unit,
    onNavigateToRecovery: () -> Unit,
    onBack: () -> Unit,
    viewModel: CredentialsViewModel
) {
    var coroutineScope = rememberCoroutineScope()
    var isLoginSuccessful by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val credentialsState by viewModel.credentialsState.collectAsState()
    val enteredCredentials = credentialsState.enteredCredentials

    var username by remember { mutableStateOf(enteredCredentials?.username ?: "") }
    var password by remember { mutableStateOf(enteredCredentials?.password ?: "") }

    var updatedUsername by remember { mutableStateOf("") }
    var updatedPassword by remember { mutableStateOf("") }

    LaunchedEffect(enteredCredentials) {
        enteredCredentials?.let {
            username = it.username
            password = it.password
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {

        UsernamePasswordLoginContent(
            username = username,
            onUsernameChange = { username = it },
            password = password,
            onPasswordChange = { password = it },
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
            onLoginClick = {
                coroutineScope.launch {
                    onLoading.invoke(true)
                    isLoginSuccessful = viewModel.isValidCredentials(username, password)
                    onLoading.invoke(false)

                    if (isLoginSuccessful) {
                        viewModel.setEnteredCredentials(username, password)

                        when {
                            username.lowercase().startsWith("bob") ||
                                    username.lowercase().startsWith("alice") ||
                                    username.lowercase().startsWith("eve") -> {
                                // Use stored credentials for successful login
                                val storedCredentials =
                                    viewModel.loadCredentialsForLogin(username, password)
                                updatedUsername = storedCredentials?.username ?: ""
                                updatedPassword = storedCredentials?.password ?: ""

                                onLoginSuccess.invoke(
                                    username,
                                    password,
                                    updatedUsername,
                                    updatedPassword
                                )

                                // Log successful login event
                                val params = Bundle().apply {
                                    putString("username", username)
                                    putString("password", password)
                                }
                                AppLogger.logEvent("successful_login", params)
                            }

                            else -> {
                                // Log for invalid username and password
                                AppLogger.logError("Unsuccessful login attempt: Invalid username and password")
                            }
                        }
                    } else {
                        // Log unsuccessful login event
                        AppLogger.logError("Unsuccessful login attempt: Login failed")
                    }
                }
            },
            onNavigateToRecovery = onNavigateToRecovery,
            onBack = onBack
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernamePasswordLoginContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRecovery: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sign In",
            style = TextStyle(
                fontSize = 16.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            OutlinedTextField(
                value = username,
                onValueChange = { onUsernameChange(it) },
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { onPasswordChange(it) },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    IconButton(
                        onClick = onTogglePasswordVisibility
                    ) {
                        Icon(
                            painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_show else R.drawable.ic_hide),
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text("Login")
            }
        }

        TextButton(
            onClick = onNavigateToRecovery,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text("Forgot Password?")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsernamePasswordLoginContentPreview() {
    val username = "bob1"
    val password = "1234567A"


    PracticeTheme {
        UsernamePasswordLoginContent(
            username = username,
            onUsernameChange = { },
            password = password,
            onPasswordChange = { password },
            isPasswordVisible = false,
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onNavigateToRecovery = {},
            onBack = {}
        )
    }
}
