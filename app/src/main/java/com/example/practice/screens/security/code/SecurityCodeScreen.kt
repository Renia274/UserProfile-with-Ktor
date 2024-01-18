package com.example.practice.screens.security.code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.ui.theme.PracticeTheme


@Composable
fun SecurityCodeScreen(
    viewModel: CredentialsViewModel,
    onNavigate: (String) -> Unit,
    securityCode: String,
) {
    var customSecurityCode by remember { mutableStateOf(securityCode) }
    var showError by remember { mutableStateOf(false) }
    var isSecurityCodeVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {



        SecurityCodeScreenContent(
            customSecurityCode = customSecurityCode,
            onNavigate=onNavigate,
            onSecurityCodeChange = { customSecurityCode = it },
            showError = showError,
            onShowErrorChange = { showError = it },
            isSecurityCodeVisible = isSecurityCodeVisible,
            onSecurityCodeVisibilityChange = { isSecurityCodeVisible = it },
            onContinueClick = {
                if (securityCode.isNotEmpty()) {
                    val savedSecurityCode = viewModel.credentialsState.value.securityCode
                    if (securityCode == savedSecurityCode) {
                        viewModel.saveSecurityCode(securityCode)
                        onNavigate.invoke("usernamePasswordLogin")
                    } else {
                        showError = true
                        println("Entered security code does not match the saved one.")
                    }
                } else {
                    showError = true
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityCodeScreenContent(
    customSecurityCode: String,
    onNavigate: (String) -> Unit,
    onSecurityCodeChange: (String) -> Unit,
    showError: Boolean,
    onShowErrorChange: (Boolean) -> Unit,
    isSecurityCodeVisible: Boolean,
    onSecurityCodeVisibilityChange: (Boolean) -> Unit,
    onContinueClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {



        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                IconButton(onClick = { onNavigate("back") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()

        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Enter Your Security code",
            style = TextStyle(fontSize = 18.sp),
            modifier = Modifier
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(32.dp))


            TextField(
                value = customSecurityCode,
                onValueChange = {
                    onSecurityCodeChange(it)
                    onShowErrorChange(false)
                },
                label = { Text("Enter Security Code") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {}),
                isError = showError,
                singleLine = true,
                visualTransformation = if (isSecurityCodeVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(32.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showError) {
            Text(
                text = "Invalid security code. Please try again.",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Continue")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecurityCodeScreenContentPreview() {
    val customSecurityCode = "123456"
    val showError = false
    val isSecurityCodeVisible = false

    PracticeTheme {
        SecurityCodeScreenContent(
            customSecurityCode = customSecurityCode,
            onNavigate = {},
            onSecurityCodeChange = {},
            showError = showError,
            onShowErrorChange = {},
            isSecurityCodeVisible = isSecurityCodeVisible,
            onSecurityCodeVisibilityChange = {},
            onContinueClick = {}
        )
    }
}
