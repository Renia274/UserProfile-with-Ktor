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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.screens.items.SaveConfirmationDialog
import com.example.practice.screens.items.SettingsField
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    sharedViewModel: SharedProfilesViewModel,
    credentialsViewModel: CredentialsViewModel,
    onNavigate: (String) -> Unit,
    onSaveCredentials: (String, String) -> Unit,
) {
    val context = LocalContext.current
    var showConfirmationDialog by remember { mutableStateOf(false) }

    var darkMode by remember {
        mutableStateOf(sharedViewModel.stateFlow.value.darkMode)
    }

    val firstName = sharedViewModel.getFirstName()
    val lastName = sharedViewModel.getLastName()

    var notificationEnabled by rememberSaveable {
        mutableStateOf(sharedViewModel.stateFlow.value.notificationEnabled)
    }
    var signupEmail by remember {
        mutableStateOf(sharedViewModel.stateFlow.value.signupEmail)
    }

    var isSecurityCodeEditable by rememberSaveable { mutableStateOf(false) }


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

    DisposableEffect(lifecycleOwner) {
        val onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit = { owner, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    //update the UI based on the lifecycle event
                    darkMode = sharedViewModel.stateFlow.value.darkMode
                    notificationEnabled = sharedViewModel.stateFlow.value.notificationEnabled
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

    SettingsContent(
        firstName = firstName,
        lastName = lastName,
        darkMode = darkMode,
        notificationEnabled = notificationEnabled,
        signupEmail = signupEmail,
        username = username,
        password = password,
        enteredSecurityCode = enteredSecurityCode,
        onNavigate = onNavigate,
        onSaveCredentials = onSaveCredentials,
        onSignupEmailChange = { sharedViewModel.setSignupEmail(it) },
        onUsernameChange = { newValue ->
            username = newValue
            credentialsViewModel.setEnteredCredentials(username = newValue, password = password)
        },
        onPasswordChange = { newValue ->
            password = newValue
            credentialsViewModel.setEnteredCredentials(username = username, password = newValue)
        },
        onSecurityCodeChange = { enteredSecurityCode = it },
        onSecurityCodeSave = {
            if (enteredSecurityCode.isNotEmpty()) {
                credentialsViewModel.saveSecurityCode(enteredSecurityCode)
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
        onShowConfirmationDialog = { showConfirmationDialog = true },
        onDismissConfirmationDialog = { showConfirmationDialog = false },
        setDarkMode = { newDarkModeState ->
            darkMode = newDarkModeState
            onNavigate("toggleDarkMode")
            val toastMessage = if (darkMode) {
                "Dark Mode is enabled"
            } else {
                "Dark Mode is disabled"
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            sharedViewModel.setDarkMode(darkMode)
        },
        setSecuritySwitch = { newSecuritySwitchState ->
            notificationEnabled = newSecuritySwitchState
            isSecurityCodeEditable = newSecuritySwitchState

            val toastMessage = if (newSecuritySwitchState) {
                "Security feature is on"
            } else {
                "Security feature is off"
            }
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            sharedViewModel.setNotificationEnabled(notificationEnabled)
        },
    )

    // Confirmation Dialog
    if (showConfirmationDialog) {
        SaveConfirmationDialog(
            onConfirm = {
                val updatedUsername = username
                val updatedPassword = password
                // Save the updated username and password
                onSaveCredentials.invoke(updatedUsername, updatedPassword)
                // update Credentials
                credentialsViewModel.updateCredentials(updatedUsername, updatedPassword)
                // navigate to login screen
                onNavigate("usernamePasswordLogin")
                showConfirmationDialog = false
            },
            onDismiss = {
                // Dismiss the dialog if the user cancels the save operation
                showConfirmationDialog = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    firstName: String,
    lastName: String,
    darkMode: Boolean,
    notificationEnabled: Boolean,
    signupEmail: String,
    username: String,
    password: String,
    enteredSecurityCode: String,
    onNavigate: (String) -> Unit,
    onSaveCredentials: (String, String) -> Unit,
    onSignupEmailChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSecurityCodeChange: (String) -> Unit,
    onSecurityCodeSave: () -> Unit,
    onShowConfirmationDialog: () -> Unit,
    onDismissConfirmationDialog: () -> Unit,
    setDarkMode: (Boolean) -> Unit,
    setSecuritySwitch: (Boolean) -> Unit,
) {


    val context = LocalContext.current


    var darkModeState by rememberSaveable { mutableStateOf(darkMode) }


    var securitySwitchState by rememberSaveable {
        mutableStateOf(notificationEnabled)
    }


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
                value = firstName,
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {})
            Spacer(modifier = Modifier.height(8.dp))

            SettingsField(
                label = "Last Name:",
                value = lastName,
                onValueChange = { /* */ },
                isEditable = false,
                onClearClick = {}
            )
            Spacer(modifier = Modifier.height(8.dp))

            SettingsField(
                label = "Email:",
                value = signupEmail,
                onValueChange = { onSignupEmailChange(it) },
                isEditable = true,
                onClearClick = { onSignupEmailChange("") })

            Spacer(modifier = Modifier.height(8.dp))


            SettingsField(
                label = "Username:",
                value = username,
                onValueChange = { newValue ->
                    onUsernameChange(newValue)
                    onSaveCredentials.invoke(newValue, password)
                },
                isEditable = true,
                onClearClick = {
                    onUsernameChange("")
                    onSaveCredentials.invoke("", password)
                })

            Spacer(modifier = Modifier.height(8.dp))


            SettingsField(
                label = "Password:",
                value = password,
                onValueChange = { newValue ->
                    onPasswordChange(newValue)
                    onSaveCredentials.invoke(username, newValue)
                },
                isEditable = true,
                onClearClick = {
                    onPasswordChange("")
                    onSaveCredentials.invoke(username, "")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Show the confirmation dialog before saving changes
                    onShowConfirmationDialog()
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Save Changes")
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
                    onSecurityCodeChange(updatedSecurityCode)
                },
                isEditable = notificationEnabled,
                onClearClick = {
                    onSecurityCodeChange("")
                },
            )

            Spacer(modifier = Modifier.height(16.dp))


            Button(
                onClick = {
                    onSecurityCodeSave()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Save Security Code")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )


            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Settings", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                "Dark Mode",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Switch(
                checked = darkModeState,
                onCheckedChange = {
                    darkModeState = !darkModeState
                    setDarkMode(darkModeState)
                    val toastMessage = if (darkModeState) {
                        "Dark Mode is enabled"
                    } else {
                        "Dark Mode is disabled"
                    }
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(8.dp))


            Text(
                "Enable Security Code",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth()
            )
            Switch(
                checked = securitySwitchState,
                onCheckedChange = {
                    securitySwitchState = !securitySwitchState
                    setSecuritySwitch(securitySwitchState)
                    val toastMessage = if (securitySwitchState) {
                        "Security feature is enabled"
                    } else {
                        "Security feature is disabled"
                    }
                    Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )


            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Composable
@Preview(showBackground = true)
fun SettingsContentPreview() {
    val darkMode = true
    val notificationEnabled = true
    val signupEmail = "john.doe@example.com"
    val username = "john_doe"
    val password = "********"
    val enteredSecurityCode = "1234"

    PracticeTheme {
        SettingsContent(
            firstName = "John",
            lastName = "Doe",
            darkMode = darkMode,
            notificationEnabled = notificationEnabled,
            signupEmail = signupEmail,
            username = username,
            password = password,
            enteredSecurityCode = enteredSecurityCode,
            onNavigate = {},
            onSaveCredentials = { _, _ -> },
            onSignupEmailChange = {},
            onUsernameChange = {},
            onPasswordChange = {},
            onSecurityCodeChange = {},
            onSecurityCodeSave = {},
            onShowConfirmationDialog = {},
            onDismissConfirmationDialog = {},
            setDarkMode = {},
            setSecuritySwitch = {}
        )
    }

}


