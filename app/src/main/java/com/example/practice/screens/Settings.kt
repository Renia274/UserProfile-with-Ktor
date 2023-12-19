package com.example.practice.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    sharedViewModel: SharedProfilesViewModel,
    credentialsViewModel: CredentialsViewModel,
    onNavigate: (String) -> Unit,
    onSaveCredentials: (String, String) -> Unit,

    ) {


    val context = LocalContext.current

    var darkMode by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(false) }


    var showConfirmationDialog by remember { mutableStateOf(false) }
    var rememberedUsernameState by remember {
        mutableStateOf(
            credentialsViewModel.enteredCredentials.value?.username ?: ""
        )
    }
    var rememberedPasswordState by remember {
        mutableStateOf(
            credentialsViewModel.enteredCredentials.value?.password ?: ""
        )
    }


    var username by remember { mutableStateOf(rememberedUsernameState) }
    var password by remember { mutableStateOf(rememberedPasswordState) }

    var enteredSecurityCode by remember {
        mutableStateOf(
            credentialsViewModel.securityCode.value ?: ""
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(title = { Text("Settings") }, navigationIcon = {
                IconButton(onClick = {
                    onNavigate("back")
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Account Information Section
            Text(
                "Account Information",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Display user account information
            ProfileField(label = "First Name:",
                value = sharedViewModel.userProfiles.firstOrNull()?.firstName ?: "",
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {})
            Spacer(modifier = Modifier.height(8.dp))

            ProfileField(label = "Last Name:",
                value = sharedViewModel.userProfiles.firstOrNull()?.lastName ?: "",
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {})

            Spacer(modifier = Modifier.height(8.dp))

            ProfileField(label = "Email:",
                value = sharedViewModel.signupEmail.value ?: "",
                onValueChange = { sharedViewModel.setSignupEmail(it) },
                isEditable = true,
                onClearClick = { sharedViewModel.setSignupEmail("") })

            Spacer(modifier = Modifier.height(8.dp))

            // Username
            ProfileField(label = "Username:", value = username, onValueChange = { newValue ->
                username = newValue
                credentialsViewModel.setEnteredCredentials(
                    username = newValue, password = password
                )
            }, isEditable = true, onClearClick = {
                username = ""
                credentialsViewModel.setEnteredCredentials(username = "", password = "")
            })

            Spacer(modifier = Modifier.height(8.dp))

            // Password
            ProfileField(label = "Password:", value = password, onValueChange = { newValue ->
                password = newValue
                credentialsViewModel.setEnteredCredentials(
                    username = username, password = newValue
                )
            }, isEditable = true, onClearClick = {
                password = ""
                credentialsViewModel.setEnteredCredentials(username = username, password = "")
            }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Show the confirmation dialog before saving changes
                    showConfirmationDialog = true
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Changes")
            }

            // Confirmation Dialog
            if (showConfirmationDialog) {
                SaveConfirmationDialog(onConfirm = {

                    val updatedUsername = username
                    val updatedPassword = password
                    // Save the updated username and password using the onSaveCredentials function
                    onSaveCredentials.invoke(updatedUsername, updatedPassword)

                    // Call the updateCredentials function in CredentialsViewModel
                    credentialsViewModel.updateCredentials(updatedUsername, updatedPassword)

                    showConfirmationDialog = false
                }, onDismiss = {
                    // Dismiss the dialog if the user cancels the save operation
                    showConfirmationDialog = false
                })
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )


            // Security Section
            Text("Security", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(16.dp))

            ProfileField(label = "Security Code:",
                value = enteredSecurityCode,
                onValueChange = { updatedSecurityCode ->
                    enteredSecurityCode = updatedSecurityCode
                },
                isEditable = true,
                onClearClick = {
                    enteredSecurityCode = ""

                })


            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    credentialsViewModel.saveSecurityCode(enteredSecurityCode)
                    onNavigate("securityCode")
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Security Code")
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text("Settings", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier = Modifier.height(16.dp))

            // Dark Mode
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Dark Mode", style = TextStyle(fontSize = 16.sp), modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = darkMode, onCheckedChange = {
                    darkMode = it
                    sharedViewModel.setDarkMode(it)
                }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Notification switch with Spacer
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Enable Notifications",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Switch(
                checked = notificationEnabled, onCheckedChange = {
                    notificationEnabled = it
                    if (it) {
                        // Display notification when notifications are enabled
                        Toast.makeText(context, "Notifications are ON", Toast.LENGTH_SHORT).show()
                    }
                }, modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun SaveConfirmationDialog(
    onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    var isSpinnerVisible by remember { mutableStateOf(false) }

    AlertDialog(onDismissRequest = {
        if (!isSpinnerVisible) {
            onDismiss.invoke()
        }
    }, title = { Text(if (isSpinnerVisible) "Saving Changes" else "Save Changes") }, text = {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
        ) {
            if (isSpinnerVisible) {
                LaunchedEffect(isSpinnerVisible) {
                    delay(2000)
                    onConfirm.invoke()
                    isSpinnerVisible = false
                }
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp)
                )
            } else {
                Text("Are you sure you want to save changes?")
            }
        }
    }, confirmButton = {
        TextButton(onClick = {
            isSpinnerVisible = true
        }) { Text("Confirm") }
    }, dismissButton = {
        TextButton(onClick = {
            onDismiss.invoke()
        }) {
            Text("Cancel")
        }
    })
}


@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    onClearClick: () -> Unit,
    initiallyVisible: Boolean = false
) {
    var isVisible by remember { mutableStateOf(initiallyVisible) }
    val observedValue by remember { mutableStateOf(value) }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
        )
        if (isEditable) {
            val editableState = remember { mutableStateOf(observedValue) }
            OutlinedTextField(
                value = editableState.value,
                onValueChange = {
                    editableState.value = it
                    onValueChange(it)
                },
                label = { Text(label) },
                visualTransformation =
                if (label.equals("Password:", ignoreCase = true) || label.equals("Security Code:", ignoreCase = true))
                    if (isVisible) VisualTransformation.None else PasswordVisualTransformation()
                else VisualTransformation.None,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = if (label.equals("Password:", ignoreCase = true))
                        KeyboardType.Password else KeyboardType.Text
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                trailingIcon = {
                    if (label.equals("Password:", ignoreCase = true) || label.equals("Security Code:", ignoreCase = true)) {
                        IconButton(
                            onClick = {
                                isVisible = !isVisible
                                onClearClick.invoke()
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = if (isVisible) R.drawable.ic_show_pin else R.drawable.ic_hide),
                                contentDescription = if (isVisible) "Hide" else "Show"
                            )
                        }
                    }
                }
            )
        } else {
            Text(text = observedValue, style = TextStyle(fontSize = 16.sp))
        }
    }
}

