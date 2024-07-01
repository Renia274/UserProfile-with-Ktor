package com.example.practice.screens.pin

import android.os.Bundle
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.R
import com.example.practice.data.UserData
import com.example.practice.logs.app.AppLogger
import com.example.practice.navigation.graph.Navigation
import com.example.practice.profiles.viewmodel.pin.PinViewModel
import com.example.practice.ui.theme.PracticeTheme


@Composable
fun PinLoginScreen(
    pinViewModel: PinViewModel = hiltViewModel(),
    onLoginSuccess: (UserData) -> Unit,
    onNavigate: (Navigation.Screen) -> Unit,
    onPostNavigate: () -> Unit,
) {
    var pin by remember { mutableStateOf("") }
    var isPinVisible by remember { mutableStateOf(false) }

    // Fetch or initialize PINs from external source
    val bobPin = pinViewModel.getPinForProfile("Bob") ?: ""
    val alicePin = pinViewModel.getPinForProfile("Alice") ?: ""
    val evePin = pinViewModel.getPinForProfile("Eve") ?: ""

    PinLoginContent(
        pin = pin,
        onPinChange = { newPin -> pin = newPin },
        isPinVisible = isPinVisible,
        onTogglePinVisibility = { isPinVisible = !isPinVisible },
        bobPin = bobPin,
        alicePin = alicePin,
        evePin = evePin,
        onLogin = { userProfile ->
            userProfile?.let {
                onLoginSuccess.invoke(it)
            } ?: run {
                // Handle incorrect PIN logic here if needed
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
    bobPin: String,
    alicePin: String,
    evePin: String,
    onLogin: (UserData?) -> Unit,
    onPostNavigate: () -> Unit
) {
    val maxPinLength = 6

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text("Enter Your PIN") },
                    navigationIcon = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = {
                            if (it.length <= maxPinLength) {
                                onPinChange(it)
                                // Log PIN input
                                AppLogger.logEvent("pin_input", Bundle().apply {
                                    putString("pin_length", it.length.toString())
                                })
                            }
                        },
                        label = { Text("Enter PIN") },
                        visualTransformation = if (isPinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.NumberPassword,
                            imeAction = ImeAction.Done
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = { onTogglePinVisibility() },
                            ) {
                                Icon(
                                    painter = painterResource(id = if (isPinVisible) R.drawable.ic_show else R.drawable.ic_hide),
                                    contentDescription = if (isPinVisible) "Hide pin" else "Show pin"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
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
                        onClick = {
                            if (pin.length == maxPinLength) {
                                val userProfile = when (pin.substring(0, 1)) {
                                    "1" -> UserData("Bob", "Johnson", R.drawable.bob_johnson)
                                    "2" -> UserData("Alice", "Smith", R.drawable.alice_smith)
                                    "3" -> UserData("Eve", "Brown", R.drawable.eve_brown)
                                    else -> null
                                }

                                // Trigger login success or reset PIN if not recognized
                                onLogin(userProfile)
                            } else {
                                // Log incorrect PIN length
                                AppLogger.logError("Incorrect PIN length: ${pin.length}")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text("Log In")
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PinLoginContentPreview() {
    PracticeTheme {
        Surface {
            PinLoginContent(
                pin = "",
                onPinChange = {},
                isPinVisible = true,
                onTogglePinVisibility = {},
                bobPin = "123456",
                alicePin = "987654",
                evePin = "555555",
                onLogin = { },
                onPostNavigate = { }
            )
        }
    }
}
