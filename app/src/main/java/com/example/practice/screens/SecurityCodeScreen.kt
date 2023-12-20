package com.example.practice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.practice.R
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityCodeScreen(
    viewModel: CredentialsViewModel,
    onNavigate: (String) -> Unit,
    securityCode: String
) {
    var customSecurityCode by remember { mutableStateOf(securityCode) }
    var showError by remember { mutableStateOf(false) }
    var isSecurityCodeVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = { Text("Enter your security code") },
            navigationIcon = {
                IconButton(onClick = { onNavigate("back") }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(64.dp))


        TextField(
            value = customSecurityCode,
            onValueChange = {
                customSecurityCode = it
                showError = false
            },
            label = { Text("Enter Security Code") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = {}
            ),
            isError = showError,
            singleLine = true,
            visualTransformation = if (isSecurityCodeVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            trailingIcon = {
                IconButton(
                    onClick = { isSecurityCodeVisible = !isSecurityCodeVisible },
                ) {
                    Icon(
                        painter = painterResource(id = if (isSecurityCodeVisible) R.drawable.ic_show_pin else R.drawable.ic_hide),
                        contentDescription = if (isSecurityCodeVisible) "Hide security code" else "Show security code"
                    )
                }
            }
        )


        if (showError) {
            Text(
                text = "Invalid security code. Please try again.",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = {
                if (customSecurityCode.isNotEmpty()) {
                    val savedSecurityCode = viewModel.securityCode.value
                    if (customSecurityCode == savedSecurityCode) {
                        // Security code is valid, save it
                        viewModel.saveSecurityCode(customSecurityCode)
                        showError=false
                        onNavigate("pinLogin")
                    } else {
                        showError = false
                        onNavigate("pinLogin")
                    }

                    // Navigate to "pinLogin" regardless of the error
                    onNavigate.invoke("pinLogin")
                } else {
                    showError = true
                    println("Entered security code is empty.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Cyan)
        ) {
            Text("Continue")
        }

    }
}