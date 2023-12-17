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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    sharedViewModel: SharedProfilesViewModel,
    credentialsViewModel: CredentialsViewModel,
    onNavigate: (String) -> Unit,

) {
    var darkMode by remember { mutableStateOf(false) }
    var notificationEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var showConfirmationDialog by remember { mutableStateOf(false) }

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
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigate("back")
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Account Information Section
            Text("Account Information", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier = Modifier.height(8.dp))

            // Display user account information from the view models using fields
            ProfileField(
                label = "First Name",
                value = sharedViewModel.userProfiles.firstOrNull()?.firstName ?: "",
                onValueChange = { /* Handle value change if needed */ },
                isEditable = false,
                onClearClick = {}
            )
            ProfileField(
                label = "Last Name",
                value = sharedViewModel.userProfiles.firstOrNull()?.lastName ?: "",
                onValueChange = { /* Handle value change if needed */ },
                isEditable = false,
                onClearClick = {}
            )
            ProfileField(
                label = "Username",
                value = credentialsViewModel.enteredCredentials.value?.username ?: "",
                onValueChange = { credentialsViewModel.setEnteredCredentials(username = it, password = "") },
                isEditable = true,
                onClearClick = { credentialsViewModel.setEnteredCredentials(username = "", password = "") }
            )
            ProfileField(
                label = "Email",
                value = sharedViewModel.signupEmail.value ?: "",
                onValueChange = { sharedViewModel.setSignupEmail(it) },
                isEditable = true,
                onClearClick = { sharedViewModel.setSignupEmail("") }
            )
            ProfileField(
                label = "Password",
                value = credentialsViewModel.enteredCredentials.value?.password ?: "",
                onValueChange = { credentialsViewModel.setEnteredCredentials(username = "", password = it) },
                isEditable = true,
                onClearClick = { credentialsViewModel.setEnteredCredentials(username = "", password = "") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Changes Button
            Button(
                onClick = {
                    // Show the confirmation dialog before saving changes
                    showConfirmationDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Changes")
            }


// Confirmation Dialog
            // Confirmation Dialog
            if (showConfirmationDialog) {
                SaveConfirmationDialog(
                    onConfirm = {
                        val updatedUsername = credentialsViewModel.enteredCredentials.value?.username ?: ""
                        val updatedPassword = credentialsViewModel.enteredCredentials.value?.password ?: ""
                        credentialsViewModel.saveEnteredCredentials(updatedUsername, updatedPassword)



                        showConfirmationDialog = false
                    },
                    onDismiss = {
                        // Dismiss the dialog if the user cancels the save operation
                        showConfirmationDialog = false
                    }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Divider above Dark Mode
            Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))

            // Title for switches using Spacer
            Spacer(modifier = Modifier.height(16.dp))
            Text("Settings", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(16.dp))

            // Dark Mode with Spacer
            Spacer(modifier = Modifier.height(8.dp))
            Text("Dark Mode", style = TextStyle(fontSize = 16.sp), modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = darkMode,
                onCheckedChange = {
                    darkMode = it
                    sharedViewModel.setDarkMode(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
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
                checked = notificationEnabled,
                onCheckedChange = {
                    notificationEnabled = it
                    if (it) {
                        // Display notification when notifications are enabled
                        Toast.makeText(context, "Notifications are ON", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            Divider(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun SaveConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Changes") },
        text = { Text("Are you sure you want to save changes?") },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm.invoke()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss.invoke()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditable: Boolean,
    onClearClick: () -> Unit
) {
    val observedValue by rememberUpdatedState(value)

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
                visualTransformation = if (label.equals("Password", ignoreCase = true)) {
                    // Use PasswordVisualTransformation for the password field
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                trailingIcon = {
                    if (editableState.value.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                editableState.value = ""
                                onClearClick.invoke()
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                        }
                    }
                }
            )
        } else {
            Text(text = observedValue, style = TextStyle(fontSize = 16.sp))
        }
    }
}