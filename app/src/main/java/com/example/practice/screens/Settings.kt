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
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
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


    var darkMode by remember {
        mutableStateOf(sharedViewModel.stateFlow.value.darkMode)
    }

    // Observe changes in notificationEnabled
    var notificationEnabled by remember {
        mutableStateOf(sharedViewModel.stateFlow.value.notificationEnabled)
    }
    var signupEmail = sharedViewModel.stateFlow.value.signupEmail



    var isSecurityCodeEditable by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    var rememberedUsernameState by remember {
        mutableStateOf(
            credentialsViewModel.credentialsState.value.enteredCredentials?.username ?: ""
        )
    }
    var rememberedPasswordState by remember {
        mutableStateOf(
            credentialsViewModel.credentialsState.value.enteredCredentials?.password ?: ""
        )
    }

    val userProfiles by sharedViewModel.userProfiles.collectAsState()

    var username by remember { mutableStateOf(rememberedUsernameState) }
    var password by remember { mutableStateOf(rememberedPasswordState) }

    var enteredSecurityCode by remember {
        mutableStateOf(
            if (notificationEnabled) {
                credentialsViewModel.credentialsState.value.securityCode ?: ""
            } else {
                ""
            }
        )
    }


    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(notificationEnabled) {
        notificationEnabled = notificationEnabled
    }

    DisposableEffect(lifecycleOwner) {
        val onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit = { owner, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_RESUME -> {
                    //update the UI based on the lifecycle event
                    darkMode = darkMode
                    notificationEnabled = notificationEnabled
                    isSecurityCodeEditable = notificationEnabled
                }

                else -> Unit
            }
        }

        // Access the lifecycle for settings
        val lifecycle = lifecycleOwner.lifecycle

        //create owner and trigger resume event with onEvent function
        val observer = LifecycleEventObserver { owner, event ->
            onEvent(owner, event)
        }

        // Add the observer to the lifecycle
        lifecycle.addObserver(observer)

        //removes the lifecycle owner when the settings screen leaves composition
        onDispose {
            lifecycle.removeObserver(observer)
        }
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

            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigate("back")
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onNavigate("permissions")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Permissions"
                        )
                    }
                },
                modifier = Modifier
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
            SettingsField(
                label = "First Name:",
                value = userProfiles.firstOrNull()?.firstName ?: "",  // Updated this line
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {})
            Spacer(modifier = Modifier.height(8.dp))

            SettingsField(
                label = "Last Name:",
                value = userProfiles.firstOrNull()?.lastName ?: "",
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsField(label = "Email:",
                value = signupEmail,
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
                value = enteredSecurityCode,
                onValueChange = { updatedSecurityCode ->
                    enteredSecurityCode = updatedSecurityCode
                },
                isEditable = isSecurityCodeEditable,
                onClearClick = {
                    enteredSecurityCode = ""
                },

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
                        Toast.makeText(context, "Dark Mode turned on", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Dark Mode turned off", Toast.LENGTH_SHORT).show()
                    }

                }, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Security switch
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
                    isSecurityCodeEditable = newNotificationEnabledState

                    sharedViewModel.setNotificationEnabled(newNotificationEnabledState)

                    if (newNotificationEnabledState) {
                        Toast.makeText(context, "Security feature is on", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Security feature is off", Toast.LENGTH_SHORT)
                            .show()
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


