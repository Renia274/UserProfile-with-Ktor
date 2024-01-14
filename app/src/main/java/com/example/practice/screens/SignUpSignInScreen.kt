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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice.ui.theme.PracticeTheme

@Composable
fun SignUpSignInScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,

) {


    SignUpSignInContent(
        onSignUpClick = {

            onSignUpClick.invoke()
        },
        onSignInClick = onSignInClick,

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
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {



        Button(
            onClick = {
                // Continue with the sign-up
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


@Preview(showBackground = true)
@Composable
fun SignUpSignInContentPreview() {

    PracticeTheme {
        SignUpSignInContent(
            onSignUpClick = { /*  */ },
            onSignInClick = { /*  */ },

        )
    }

}
