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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.screens.items.SaveConfirmationDialog
import com.example.practice.screens.items.SettingsField
import kotlinx.coroutines.launch

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
    var notificationEnabled by remember {
        mutableStateOf(
            sharedViewModel.notificationEnabled.value ?: false
        )
    }
    var isSecurityCodeEditable by remember { mutableStateOf(false) }
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
            if (notificationEnabled) {
                credentialsViewModel.securityCode.value ?: ""
            } else {
                ""
            }
        )
    }


    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


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
            SettingsField(label = "First Name:",
                value = sharedViewModel.userProfiles.firstOrNull()?.firstName ?: "",
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {})
            Spacer(modifier = Modifier.height(8.dp))

            SettingsField(label = "Last Name:",
                value = sharedViewModel.userProfiles.firstOrNull()?.lastName ?: "",
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {})

            Spacer(modifier = Modifier.height(8.dp))

            SettingsField(label = "Email:",
                value = sharedViewModel.signupEmail.value ?: "",
                onValueChange = { sharedViewModel.setSignupEmail(it) },
                isEditable = true,
                onClearClick = { sharedViewModel.setSignupEmail("") })

            Spacer(modifier = Modifier.height(8.dp))

            // Username
            SettingsField(label = "Username:", value = username, onValueChange = { newValue ->
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
            SettingsField(label = "Password:", value = password, onValueChange = { newValue ->
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
                    // Save the updated username and password
                    onSaveCredentials.invoke(updatedUsername, updatedPassword)

                    //update Credentials
                    credentialsViewModel.updateCredentials(updatedUsername, updatedPassword)

                    //navigate to login screen
                    onNavigate("usernamePasswordLogin")

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



            SettingsField(
                label = "Security Code:",
                value = if (isSecurityCodeEditable) enteredSecurityCode else "********", // Show actual code in edit mode, otherwise hide it
                onValueChange = { updatedSecurityCode ->
                    enteredSecurityCode = updatedSecurityCode
                },
                isEditable = isSecurityCodeEditable,
                onClearClick = {
                    // Clear the entered security code
                    enteredSecurityCode = ""
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (enteredSecurityCode.isNotEmpty()) {
                        // Save the entered security code
                        credentialsViewModel.saveSecurityCode(enteredSecurityCode)

                        // Navigate to the security code screen
                        onNavigate("securityCode")

                        Toast.makeText(context, "Security Code saved", Toast.LENGTH_SHORT).show()
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                "Security code cannot be empty",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Security Code")
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))


            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            Text("Settings", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier = Modifier.height(8.dp))


            // Dark Mode
            Text(
                "Dark Mode", style = TextStyle(fontSize = 16.sp), modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Switch(
                checked = darkMode, onCheckedChange = {
                    darkMode = it
                    sharedViewModel.setDarkMode(it)
                    if (it) {
                        // Display toast when Dark Mode is turned on
                        Toast.makeText(context, "Dark Mode turned on", Toast.LENGTH_SHORT).show()
                    } else {
                        // Display toast when Dark Mode is turned off
                        Toast.makeText(context, "Dark Mode turned off", Toast.LENGTH_SHORT).show()
                    }

                }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Security  switch
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Enable Security Code",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Switch(
                checked = notificationEnabled,
                onCheckedChange = { newNotificationEnabledState ->
                    notificationEnabled = newNotificationEnabledState
                    isSecurityCodeEditable =
                        newNotificationEnabledState // Update the editability of the security code field

                    sharedViewModel.setNotificationEnabled(newNotificationEnabledState)

                    if (newNotificationEnabledState) {
                        // Display a message when the security feature is enabled
                        Toast.makeText(context, "Security feature is on", Toast.LENGTH_SHORT).show()
                    } else {
                        // Display a message when the security feature is disabled
                        Toast.makeText(context, "Security feature is off", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}




