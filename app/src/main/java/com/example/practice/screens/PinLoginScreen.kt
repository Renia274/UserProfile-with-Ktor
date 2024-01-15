package com.example.practice.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.data.UserData
import com.example.practice.navigation.graph.Navigation
import com.example.practice.ui.theme.PracticeTheme


@Composable
fun PinLoginScreen(
    onLoginSuccess: (UserData) -> Unit,
    onNavigate: (Navigation.Screen) -> Unit,
    onPostNavigate: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    var isPinVisible by remember { mutableStateOf(false) }

    PinLoginContent(
        pin = pin,
        onPinChange = { newPin -> pin = newPin },
        isPinVisible = isPinVisible,
        onTogglePinVisibility = { isPinVisible = !isPinVisible },
        onLogin = { userProfile ->
            userProfile?.let {
                onLoginSuccess.invoke(it)
            } ?: run {
                pin = ""
            }
        },
        onPostNavigate = onPostNavigate
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinLoginContent(
    pin: String,
    onPinChange: (String) -> Unit,
    isPinVisible: Boolean,
    onTogglePinVisibility: () -> Unit,
    onLogin: (UserData?) -> Unit,
    onPostNavigate: () -> Unit
) {
    val maxPinLength = 6

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("") },
            navigationIcon = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(modifier = Modifier.weight(1f))

                    // Navigate to postlist screen
                    IconButton(
                        onClick = { onPostNavigate.invoke() }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Enter Your Pin",
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
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = pin,
                onValueChange = {
                    if (it.length <= maxPinLength) {
                        onPinChange(it)
                    }
                },
                label = { Text("Enter PIN") },
                visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.width(4.dp))

                        IconButton(
                            onClick = { onTogglePinVisibility() },
                        ) {
                            Icon(
                                painter = painterResource(id = if (isPinVisible) R.drawable.ic_show else R.drawable.ic_hide),
                                contentDescription = if (isPinVisible) "Hide pin" else "Show pin"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val userProfile = when (pin.lowercase()) {
                    "123456" -> UserData("Bob", "Johnson", R.drawable.bob_johnson)
                    "987654" -> UserData("Alice", "Smith", R.drawable.alice_smith)
                    "555555" -> UserData("Eve", "Brown", R.drawable.eve_brown)
                    else -> null
                }

                // Trigger login success or reset PIN if not recognized
                onLogin(userProfile)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log In")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}


@Preview(showBackground = true)
@Composable
fun PinLoginContentPreview() {
    PracticeTheme {
        Surface {
            PinLoginContent(
                pin = "123456",
                onPinChange = { /*  */ },
                isPinVisible = true,
                onTogglePinVisibility = { /*  */ },
                onLogin = { /*  */ },
                onPostNavigate = { /*  */ }
            )
        }
    }
}

