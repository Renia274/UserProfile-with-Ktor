package com.example.practice.screens.signup

import android.os.Bundle
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.logs.app.AppLogger
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.validators.isValidEmail
import com.example.practice.validators.isValidPassword
import kotlinx.coroutines.launch


@Composable
fun SignupScreen(
    onNavigateToLogin: () -> Unit,
    onNavigate: () -> Unit,
    credentialsViewModel: CredentialsViewModel,
    sharedViewModel: SharedProfilesViewModel,
) {
    var scope = rememberCoroutineScope()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        SignupContent(
            username = username,
            onUsernameChange = { username = it },
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            isPasswordVisible = isPasswordVisible,
            onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible },
            onSignUpClick = {
                val params = Bundle().apply {
                    putString("password", password)
                    putString("email", email)
                }

                val isEmailValid = isValidEmail(email)
                val isPasswordValid = isValidPassword(password)

                if (isEmailValid && isPasswordValid) {
                    scope.launch {
                        AppLogger.logEvent("successful_signup", params)
                        credentialsViewModel.setEnteredCredentials(username, password)
                        credentialsViewModel.saveUserCredentials(username, password)
                        sharedViewModel.setSignupEmail(email)
                        onNavigate.invoke()
                    }
                } else {
                    AppLogger.logEvent("Invalid email or password format", params)
                    AppLogger.logError("signup failed")
                    showError = true
                }
            },
            showError = showError,
            onDismissError = { showError = false }
        )

        // Sign in if there is already a registered account
        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Already have an account? Sign In")
        }
    }
}

@Composable
fun SignupContent(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onSignUpClick: () -> Unit,
    showError: Boolean,
    onDismissError: () -> Unit
) {
    val h4 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )

    val emailBorderColor = if (email.isNotEmpty() && !isValidEmail(email)) Color.Red else Color.Transparent
    val passwordBorderColor = if (password.isNotEmpty() && !isValidPassword(password)) Color.Red else Color.Transparent

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 32.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create an Account",
            style = h4,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Username") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                onEmailChange(it)
                if (isValidEmail(it)) {
                    onDismissError()
                }
            },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, emailBorderColor, shape = MaterialTheme.shapes.medium),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                onPasswordChange(it)
                if (isValidPassword(it)) {
                    onDismissError()
                }
            },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        painter = painterResource(id = if (isPasswordVisible) R.drawable.ic_show else R.drawable.ic_hide),
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, passwordBorderColor, shape = MaterialTheme.shapes.medium),
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Show error message if the mail or password is invalid
        if (showError) {
            Text(
                text = "Invalid email or password format",
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}




@Preview(showBackground = true)
@Composable
fun SignupContentPreview() {
    var username = ""
    var email = ""
    var password = ""
    var isPasswordVisible = false

    SignupContent(
        username = username,
        onUsernameChange = { username = it },
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        isPasswordVisible = isPasswordVisible,
        onTogglePasswordVisibility = {},
        onSignUpClick = {},
        showError = false,
        onDismissError = {}
    )
}
