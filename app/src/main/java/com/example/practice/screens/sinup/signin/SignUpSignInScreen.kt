package com.example.practice.screens.sinup.signin

import android.os.Bundle
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.logs.app.AppLogger
import com.example.practice.ui.theme.PracticeTheme

@Composable
fun SignUpSignInScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    //log button click events
    fun logButtonClick(buttonName: String) {
        val params = Bundle().apply {
            putString("button_name", buttonName)
        }
        AppLogger.logEvent("button_clicked", params)
    }

    SignUpSignInContent(
        onSignUpClick = {
            onSignUpClick.invoke()
            // Log sign-up button click
            logButtonClick("Sign Up")
        },
        onSignInClick = {
            onSignInClick.invoke()
            // Log sign-in button click
            logButtonClick("Sign In")
        },
    )
}



@Composable
fun SignUpSignInContent(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    // Continue with the sign-up
                    onSignUpClick.invoke()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Green)
            ) {
                Text("Sign Up", color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onSignInClick,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Sign In", color = Color.White)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpSignInContentPreview() {

    PracticeTheme {
        SignUpSignInContent(
            onSignUpClick = {},
            onSignInClick = {},

        )
    }

}
