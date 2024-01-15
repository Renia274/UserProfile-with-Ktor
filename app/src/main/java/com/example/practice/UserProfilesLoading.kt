package com.example.practice


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practice.data.UserData
import com.example.practice.navigation.bottom.handler.navigateTo
import com.example.practice.navigation.bottom.navigation.BottomNavigationItems
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import com.example.practice.screens.EditProfile
import com.example.practice.screens.items.CustomCountDownTimer
import com.example.practice.screens.items.UserProfileItem
import com.example.practice.utils.SignOutDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesLoading(
    userProfiles: StateFlow<List<UserData>>,
    viewModel: SharedProfilesViewModel ,
    timerViewModel: TimerViewModel ,
    credentialsViewModel: CredentialsViewModel ,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    username: String,
    topAppBarTitle: String,
) {
    // State for showing the edit profile screen
    var isShowingEdit by remember { mutableStateOf(false) }


    var showSignOutDialog by remember { mutableStateOf(false) }


    val initialSelectedIndex = 0
    val selectedIndexFlow = remember { MutableStateFlow(initialSelectedIndex) }


    val timerState by timerViewModel.stateFlow.collectAsState()
    val timeLeft = timerState.timeLeft

    // Effect to check if the timer has run out and trigger navigation
    LaunchedEffect(timeLeft) {
        if (timerViewModel.stateFlow.value.timeLeft <= 0) {
            onNavigate("usernamePasswordLogin")
            timerViewModel.resetTimer()
        }
    }


    val mainBackgroundColor by remember(username) {
        derivedStateOf {
            if (viewModel.stateFlow.value.darkMode) {
                Color.White
            } else {
                when {
                    username.lowercase().startsWith("bob") -> Color.Green
                    username.lowercase().startsWith("alice") -> Color.LightGray
                    username.lowercase().startsWith("eve") -> Color.Magenta
                    else -> Color.Gray
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            TopAppBar(
                title = {
                    Text(
                        text = topAppBarTitle,
                        color = when {
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
                    // triggers the sign-out dialog
                    IconButton(onClick = {
                        showSignOutDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }

                    // navigates to the InfoScreen
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

                CustomCountDownTimer(timerViewModel = timerViewModel)
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                // Choose between UserProfileItem and EditProfile based on edit mode
                if (selectedIndexFlow.value in userProfiles.value.indices && !isShowingEdit) {
                    UserProfileItem(
                        userProfile = userProfiles.value[selectedIndexFlow.value],
                        onEditClick = {
                            isShowingEdit = true
                        },
                        isEditScreen = isShowingEdit,
                        onSaveProfession = { updatedProfession ->
                            val userProfile = userProfiles.value[selectedIndexFlow.value]
                            viewModel.saveProfession(userProfile.imageResId, updatedProfession)
                        },
                        onInterestsSelected = { selectedInterests ->
                            val userProfile = userProfiles.value[selectedIndexFlow.value]
                            viewModel.saveInterests(userProfile.imageResId, selectedInterests)
                        },
                        viewModel = viewModel
                    )
                } else {
                    EditProfile(
                        userProfiles.value,
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
                        credentialsViewModel.performSignOut()
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

