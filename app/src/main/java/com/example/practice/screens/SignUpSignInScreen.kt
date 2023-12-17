package com.example.practice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel

@Composable
fun SignUpSignInScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    viewModel: CredentialsViewModel = hiltViewModel()
) {
    // Define the username and password variables
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sign Up Button with a green background color
        Button(
            onClick = {
                // Save entered credentials to ViewModel
                viewModel.setEnteredCredentials(username, password)

                // Continue with the sign-up logic
                onSignUpClick.invoke()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Green)
        ) {
            Text("Sign Up", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign In Button with a blue background color
        Button(
            onClick = onSignInClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Sign In", color = Color.White)
        }
    }
}
