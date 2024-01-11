package com.example.practice.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.R
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernamePasswordLoginScreen(
    onLoginSuccess: (String, String, String, String) -> Unit,
    onLoading: (Boolean) -> Unit,
    onNavigateToRecovery: () -> Unit,
    onBack: () -> Unit,
    viewModel: CredentialsViewModel = hiltViewModel()
) {
    val overrideFontPadding = PlatformTextStyle(includeFontPadding = false)

    val h4 = TextStyle(
        fontSize = 16.sp,
        platformStyle = overrideFontPadding
    )

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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sign In",
                style = h4,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible },
                    ) {
                        Icon(
                            painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_show else R.drawable.ic_hide),
                            contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Set loading state to true before initiating login
                    onLoading.invoke(true)

                    isLoginSuccessful = viewModel.isValidCredentials(username, password)

                    // Set loading state to false after login attempt
                    onLoading.invoke(false)

                    if (isLoginSuccessful) {
                        // Save entered credentials to ViewModel
                        viewModel.setEnteredCredentials(username, password)

                        when {
                            username.lowercase().startsWith("bob") ||
                                    username.lowercase().startsWith("alice") ||
                                    username.lowercase().startsWith("eve") -> {
                                println("Login Successful for $username")
                                updatedUsername = username
                                updatedPassword = password


                                onLoginSuccess.invoke(
                                    username,
                                    password,
                                    updatedUsername,
                                    updatedPassword
                                )

                            }

                            else -> {
                                println("Invalid username")
                            }
                        }


                    } else {
                        println("Login Failed")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Login")
            }

            // Forgot Password TextButton
            TextButton(
                onClick = onNavigateToRecovery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Forgot Password?")
            }
        }
    }
}
