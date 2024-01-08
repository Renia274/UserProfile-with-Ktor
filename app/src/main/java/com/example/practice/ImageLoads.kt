package com.example.practice

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.data.UserData
import com.example.practice.navigation.bottom.handler.navigateTo
import com.example.practice.navigation.bottom.navigation.BottomNavigationItems
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import com.example.practice.screens.SplashWaitTimeMillis
import com.example.practice.screens.items.CameraPermissionDialog
import com.example.practice.screens.items.CountDownTimer
import com.example.practice.screens.items.CustomVerticalGrid
import com.example.practice.screens.items.DropDownList
import com.example.practice.screens.items.InterestsDropDownList
import com.example.practice.screens.items.MicrophonePermissionDialog
import com.example.practice.screens.items.UserProfileItem
import com.example.practice.utils.SignOutDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesLoading(
    userProfiles: List<UserData>,
    viewModel: SharedProfilesViewModel = hiltViewModel(),
    timerViewModel: TimerViewModel = hiltViewModel(),
    credentialsViewModel: CredentialsViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    username: String,
    topAppBarTitle: String,
) {

    val selectedIndex by remember { mutableStateOf(0) }
    var isShowingEdit by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    // Provide an initial value for selectedIndex
    val initialSelectedIndex = 0
    val selectedIndexFlow = remember { MutableStateFlow(initialSelectedIndex) }

    LaunchedEffect(isLoading) {
        delay(SplashWaitTimeMillis)
        isLoading = false
    }

    // Observe the dark mode value from the view model
    val darkMode by viewModel.darkMode.collectAsState(false)

    val timeLeft = timerViewModel.timeLeft.value

    LaunchedEffect(timeLeft) {
        // Check if the timer has run out
        if (timeLeft <= 0) {
            // Update the current destination
            onNavigate("usernamePasswordLogin")

            // Reset the timer
            timerViewModel.resetTimer()
        }
    }

    val mainBackgroundColor = if (darkMode) {
        MaterialTheme.colorScheme.background
    } else {
        when {
            username.lowercase().startsWith("bob") -> Color.Green
            username.lowercase().startsWith("alice") -> Color.LightGray
            username.lowercase().startsWith("eve") -> Color.Magenta
            else -> Color.Gray
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = topAppBarTitle, color = when {
                                username.lowercase().startsWith("bob") -> Color.Green
                                username.lowercase().startsWith("alice") -> Color.LightGray
                                username.lowercase().startsWith("eve") -> Color.Magenta
                                else -> Color.Gray
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    },
                    actions = {
                        // Perform sign-out when the sign-out icon is clicked
                        IconButton(onClick = {
                            showSignOutDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                        }

                        // Navigate to the InfoScreen when the info icon is clicked
                        IconButton(onClick = {
                            onNavigate("info")
                        }) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null)
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {


                    // Countdown timer display
                    CountDownTimer(timerViewModel = timerViewModel)

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    if (selectedIndex in userProfiles.indices && !isShowingEdit) {
                        UserProfileItem(
                            userProfile = userProfiles[selectedIndex],
                            onEditClick = {
                                isShowingEdit = true
                            },
                            isEditScreen = isShowingEdit,
                            onSaveProfession = { updatedProfession ->
                                userProfiles[selectedIndex].profession = updatedProfession
                                viewModel.saveProfession(
                                    userProfiles[selectedIndex].imageResId, updatedProfession
                                )
                            },
                            onInterestsSelected = { selectedInterests ->
                                userProfiles[selectedIndex].interests = selectedInterests
                                viewModel.saveInterests(
                                    userProfiles[selectedIndex].imageResId, selectedInterests
                                )
                            },
                            viewModel = viewModel
                        )
                    } else {
                        UserProfilesList(
                            userProfiles,
                            onBackNavigate = onBack,
                            isEditScreen = isShowingEdit,
                            viewModel = viewModel
                        )
                    }
                }

                if (showSignOutDialog) {
                    SignOutDialog(
                        viewModel = credentialsViewModel,
                        onSignOut = {
                            credentialsViewModel.performSignOut() // Perform sign-out

                            showSignOutDialog = false // Dismiss the dialog after sign-out
                            onNavigate("usernamePasswordLogin")
                                    },
                        onDismiss = {
                            showSignOutDialog = false // Dismiss the dialog if canceled
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                BottomNavigationItems(selectedIndexFlow) { navEvent ->
                    navigateTo(navEvent, onBack, onNavigate)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun UserProfileBob(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    onInterestsSelected: (List<String>) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    var isEditingInterests by remember { mutableStateOf(false) }
    var selectedInterests by remember { mutableStateOf(userProfile.interests) }
    var isInterestsDropDownListVisible by remember { mutableStateOf(false) }


    var isCameraPermissionDialogShown by rememberSaveable { mutableStateOf(true) }
    var isMicrophonePermissionDialogShown by rememberSaveable { mutableStateOf(true) }

    var isDelayApplied by remember { mutableStateOf(false) }

    val imageSize by animateDpAsState(
        targetValue = if (isEditingProfession) 150.dp else 200.dp,
        animationSpec = tween(durationMillis = 500),
        label = "imageSizeAnimation"
    )

    LaunchedEffect(key1 = Unit) {
        if (!isDelayApplied) {
            delay(2000)
            isDelayApplied = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                })





        when {
            isEditScreen && isEditingProfession -> {
                OutlinedTextField(value = selectedProfession,
                    onValueChange = { newProfession ->
                        selectedProfession = newProfession
                    },
                    label = { Text("Enter Profession") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isDropDownListVisible = !isDropDownListVisible
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    })

                if (isDropDownListVisible) {
                    DropDownList(onOptionSelected = { selectedProfession = it },
                        expanded = isDropDownListVisible,
                        onDismissRequest = { isDropDownListVisible = false })
                }
            }

            isEditingInterests -> {
                InterestsDropDownList(onInterestsSelected = { selectedInterests = it },
                    selectedInterests = selectedInterests,
                    expanded = isInterestsDropDownListVisible,
                    onDismissRequest = { isInterestsDropDownListVisible = false })

                OutlinedTextField(value = selectedInterests.joinToString(", "),
                    onValueChange = { newInterests ->
                        selectedInterests = newInterests.split(", ").map { it.trim() }
                    },
                    label = { Text("Enter Interests") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isInterestsDropDownListVisible = !isInterestsDropDownListVisible
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    })

            }

            else -> {
                Text(
                    text = "First Name: ${userProfile.firstName}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last Name: ${userProfile.lastName}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                if (userProfile.profession.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Profession: ${userProfile.profession}",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                if (userProfile.interests.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Interests: ${userProfile.interests}",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                if (!isEditScreen) {
                    CustomVerticalGrid(
                        items = listOf(
                            "Item 1",
                            "Item 2",
                            "Item 3",
                            "Item 4",
                            "Item 5",
                            "Item 6",
                            "Item 7",
                            "Item 8"
                        )
                    )
                }
                if (isDelayApplied && !isEditScreen) {

                    // Show CameraPermissionDialog if camera permissions are not granted
                    if (isCameraPermissionDialogShown) {
                        CameraPermissionDialog(isPermissionDialogShown = isCameraPermissionDialogShown,
                            onDismiss = { isCameraPermissionDialogShown = false },
                            onAllow = {
                                // Handle camera permission granted
                                isCameraPermissionDialogShown = false
                            },
                            onDeny = {
                                // Handle camera permission denied
                                isCameraPermissionDialogShown = false
                            },
                            onLater = {
                                // Handle camera permission later
                                isCameraPermissionDialogShown = false
                            })

                    }


                    // Show MicrophonePermissionDialog if microphone permissions are not granted
                    if (isMicrophonePermissionDialogShown) {
                        MicrophonePermissionDialog(isPermissionDialogShown = isMicrophonePermissionDialogShown,
                            onDismiss = { isMicrophonePermissionDialogShown = false },
                            onAllow = {
                                // Handle microphone permission granted
                                isMicrophonePermissionDialogShown = false
                            },
                            onDeny = {
                                // Handle microphone permission denied
                                isMicrophonePermissionDialogShown = false
                            },
                            onLater = {
                                // Handle microphone permission later
                                isMicrophonePermissionDialogShown = false
                            })
                    }
                }
            }

        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (isEditScreen) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isEditingProfession) {

                Button(
                    onClick = {
                        onSaveProfession(selectedProfession)
                        isEditingProfession = false
                        viewModel.saveProfession(userProfile.imageResId, selectedProfession)
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("Save Profession", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        isEditingProfession = !isEditingProfession
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Edit Profession", color = Color.White)
                }
            }

            if (isEditingInterests) {
                Button(
                    onClick = {
                        onInterestsSelected(selectedInterests)
                        viewModel.saveInterests(userProfile.imageResId, selectedInterests)
                        isEditingInterests = false
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("Save Interests", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        isEditingInterests = !isEditingInterests
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Edit Interests", color = Color.White)
                }
            }
        }

        CustomVerticalGrid(
            items = listOf(
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"
            )
        )

    }
}


@Composable
fun UserProfileAlice(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    onInterestsSelected: (List<String>) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    var isEditingInterests by remember { mutableStateOf(false) }
    var selectedInterests by remember { mutableStateOf(userProfile.interests) }
    var isInterestsDropDownListVisible by remember { mutableStateOf(false) }


    var isCameraPermissionDialogShown by rememberSaveable { mutableStateOf(true) }
    var isMicrophonePermissionDialogShown by rememberSaveable { mutableStateOf(true) }

    var isDelayApplied by remember { mutableStateOf(false) }

    val imageSize by animateDpAsState(
        targetValue = if (isEditingProfession) 150.dp else 200.dp,
        animationSpec = tween(durationMillis = 500),
        label = "imageSizeAnimation"
    )

    LaunchedEffect(key1 = Unit) {
        if (!isDelayApplied) {
            delay(2000)
            isDelayApplied = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                })


        when {
            isEditScreen && isEditingProfession -> {
                OutlinedTextField(value = selectedProfession,
                    onValueChange = { newProfession ->
                        selectedProfession = newProfession
                    },
                    label = { Text("Enter Profession") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isDropDownListVisible = !isDropDownListVisible
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    })

                if (isDropDownListVisible) {
                    DropDownList(onOptionSelected = { selectedProfession = it },
                        expanded = isDropDownListVisible,
                        onDismissRequest = { isDropDownListVisible = false })
                }
            }

            isEditingInterests -> {
                InterestsDropDownList(onInterestsSelected = { selectedInterests = it },
                    selectedInterests = selectedInterests,
                    expanded = isInterestsDropDownListVisible,
                    onDismissRequest = { isInterestsDropDownListVisible = false })

                OutlinedTextField(value = selectedInterests.joinToString(", "),
                    onValueChange = { newInterests ->
                        selectedInterests = newInterests.split(", ").map { it.trim() }
                    },
                    label = { Text("Enter Interests") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isInterestsDropDownListVisible = !isInterestsDropDownListVisible
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    })

            }

            else -> {
                Text(
                    text = "First Name: ${userProfile.firstName}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last Name: ${userProfile.lastName}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                if (userProfile.profession.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Profession: ${userProfile.profession}",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                if (userProfile.interests.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Interests: ${userProfile.interests}",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                if (!isEditScreen) {
                    CustomVerticalGrid(
                        items = listOf(
                            "Item 1",
                            "Item 2",
                            "Item 3",
                            "Item 4",
                            "Item 5",
                            "Item 6",
                            "Item 7",
                            "Item 8"
                        )
                    )
                }
                if (isDelayApplied && !isEditScreen) {

                    // Show CameraPermissionDialog if camera permissions are not granted
                    if (isCameraPermissionDialogShown) {
                        CameraPermissionDialog(isPermissionDialogShown = isCameraPermissionDialogShown,
                            onDismiss = { isCameraPermissionDialogShown = false },
                            onAllow = {
                                // Handle camera permission granted
                                isCameraPermissionDialogShown = false
                            },
                            onDeny = {
                                // Handle camera permission denied
                                isCameraPermissionDialogShown = false
                            },
                            onLater = {
                                // Handle camera permission later
                                isCameraPermissionDialogShown = false
                            })

                    }


                    // Show MicrophonePermissionDialog if microphone permissions are not granted
                    if (isMicrophonePermissionDialogShown) {
                        MicrophonePermissionDialog(isPermissionDialogShown = isMicrophonePermissionDialogShown,
                            onDismiss = { isMicrophonePermissionDialogShown = false },
                            onAllow = {
                                // Handle microphone permission granted
                                isMicrophonePermissionDialogShown = false
                            },
                            onDeny = {
                                // Handle microphone permission denied
                                isMicrophonePermissionDialogShown = false
                            },
                            onLater = {
                                // Handle microphone permission later
                                isMicrophonePermissionDialogShown = false
                            })
                    }
                }
            }

        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (isEditScreen) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isEditingProfession) {

                Button(
                    onClick = {
                        onSaveProfession(selectedProfession)
                        isEditingProfession = false
                        viewModel.saveProfession(userProfile.imageResId, selectedProfession)
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("Save Profession", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        isEditingProfession = !isEditingProfession
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Edit Profession", color = Color.White)
                }
            }

            if (isEditingInterests) {
                Button(
                    onClick = {
                        onInterestsSelected(selectedInterests)
                        viewModel.saveInterests(userProfile.imageResId, selectedInterests)
                        isEditingInterests = false
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("Save Interests", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        isEditingInterests = !isEditingInterests
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Edit Interests", color = Color.White)
                }
            }
        }

        CustomVerticalGrid(
            items = listOf(
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"
            )
        )

    }
}

@Composable
fun UserProfileEve(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    onInterestsSelected: (List<String>) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    var isEditingInterests by remember { mutableStateOf(false) }
    var selectedInterests by remember { mutableStateOf(userProfile.interests) }
    var isInterestsDropDownListVisible by remember { mutableStateOf(false) }


    var isCameraPermissionDialogShown by rememberSaveable { mutableStateOf(true) }
    var isMicrophonePermissionDialogShown by rememberSaveable { mutableStateOf(true) }

    var isDelayApplied by remember { mutableStateOf(false) }

    val imageSize by animateDpAsState(
        targetValue = if (isEditingProfession) 150.dp else 200.dp,
        animationSpec = tween(durationMillis = 500),
        label = "imageSizeAnimation"
    )

    LaunchedEffect(key1 = Unit) {
        if (!isDelayApplied) {
            delay(2000)
            isDelayApplied = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                })





        when {
            isEditScreen && isEditingProfession -> {
                OutlinedTextField(value = selectedProfession,
                    onValueChange = { newProfession ->
                        selectedProfession = newProfession
                    },
                    label = { Text("Enter Profession") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isDropDownListVisible = !isDropDownListVisible
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    })

                if (isDropDownListVisible) {
                    DropDownList(onOptionSelected = { selectedProfession = it },
                        expanded = isDropDownListVisible,
                        onDismissRequest = { isDropDownListVisible = false })
                }
            }

            isEditingInterests -> {
                InterestsDropDownList(onInterestsSelected = { selectedInterests = it },
                    selectedInterests = selectedInterests,
                    expanded = isInterestsDropDownListVisible,
                    onDismissRequest = { isInterestsDropDownListVisible = false })

                OutlinedTextField(value = selectedInterests.joinToString(", "),
                    onValueChange = { newInterests ->
                        selectedInterests = newInterests.split(", ").map { it.trim() }
                    },
                    label = { Text("Enter Interests") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = {
                            isInterestsDropDownListVisible = !isInterestsDropDownListVisible
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        }
                    })

            }

            else -> {
                Text(
                    text = "First Name: ${userProfile.firstName}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Last Name: ${userProfile.lastName}",
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                if (userProfile.profession.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Profession: ${userProfile.profession}",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                if (userProfile.interests.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Interests: ${userProfile.interests}",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                }

                if (!isEditScreen) {
                    CustomVerticalGrid(
                        items = listOf(
                            "Item 1",
                            "Item 2",
                            "Item 3",
                            "Item 4",
                            "Item 5",
                            "Item 6",
                            "Item 7",
                            "Item 8"
                        )
                    )
                }
                if (isDelayApplied && !isEditScreen) {

                    // Show CameraPermissionDialog if camera permissions are not granted
                    if (isCameraPermissionDialogShown) {
                        CameraPermissionDialog(isPermissionDialogShown = isCameraPermissionDialogShown,
                            onDismiss = { isCameraPermissionDialogShown = false },
                            onAllow = {
                                // Handle camera permission granted
                                isCameraPermissionDialogShown = false
                            },
                            onDeny = {
                                // Handle camera permission denied
                                isCameraPermissionDialogShown = false
                            },
                            onLater = {
                                // Handle camera permission later
                                isCameraPermissionDialogShown = false
                            })

                    }


                    // Show MicrophonePermissionDialog if microphone permissions are not granted
                    if (isMicrophonePermissionDialogShown) {
                        MicrophonePermissionDialog(isPermissionDialogShown = isMicrophonePermissionDialogShown,
                            onDismiss = { isMicrophonePermissionDialogShown = false },
                            onAllow = {
                                // Handle microphone permission granted
                                isMicrophonePermissionDialogShown = false
                            },
                            onDeny = {
                                // Handle microphone permission denied
                                isMicrophonePermissionDialogShown = false
                            },
                            onLater = {
                                // Handle microphone permission later
                                isMicrophonePermissionDialogShown = false
                            })
                    }
                }
            }

        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (isEditScreen) {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isEditingProfession) {

                Button(
                    onClick = {
                        onSaveProfession(selectedProfession)
                        isEditingProfession = false
                        viewModel.saveProfession(userProfile.imageResId, selectedProfession)
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("Save Profession", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        isEditingProfession = !isEditingProfession
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Edit Profession", color = Color.White)
                }
            }

            if (isEditingInterests) {
                Button(
                    onClick = {
                        onInterestsSelected(selectedInterests)
                        viewModel.saveInterests(userProfile.imageResId, selectedInterests)
                        isEditingInterests = false
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue
                    )
                ) {
                    Text("Save Interests", color = Color.White)
                }
            } else {
                Button(
                    onClick = {
                        isEditingInterests = !isEditingInterests
                    }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Edit Interests", color = Color.White)
                }
            }
        }

        CustomVerticalGrid(
            items = listOf(
                "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8"
            )
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesList(
    userProfiles: List<UserData>,
    onBackNavigate: () -> Unit,
    isEditScreen: Boolean,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(title = { Text("Edit") }, navigationIcon = {
            IconButton(onClick = { onBackNavigate() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
        Spacer(modifier = Modifier.height(16.dp))

        userProfiles.forEach { userProfile ->
            UserProfileItem(
                userProfile,
                onEditClick = {},
                isEditScreen,
                onSaveProfession = { updatedProfession ->
                    userProfile.profession = updatedProfession
                    viewModel.saveProfession(userProfile.imageResId, updatedProfession)
                },
                onInterestsSelected = { selectedInterests ->
                    userProfile.interests = selectedInterests
                    viewModel.saveInterests(userProfile.imageResId, selectedInterests)
                },
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
