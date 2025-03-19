package com.example.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.practice.data.UserData
import com.example.practice.navigation.bottom.navItems.BottomNavItem
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import com.example.practice.screens.userprofile.editprofile.EditProfile
import com.example.practice.screens.userprofile.profile.components.CustomBottomBar
import com.example.practice.screens.userprofile.profile.components.CustomCountDownTimer
import com.example.practice.screens.userprofile.profile.components.SignOutDialog
import com.example.practice.screens.userprofile.profile.components.UserProfileItem
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesLoading(
    userProfiles: StateFlow<List<UserData>>,
    viewModel: SharedProfilesViewModel,
    timerViewModel: TimerViewModel,
    credentialsViewModel: CredentialsViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    username: String,
    topAppBarTitle: String,
) {
    var isShowingEdit by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    val timerState by timerViewModel.stateFlow.collectAsState()
    val timeLeft = timerState.timeLeft
    val userProfilesValue by userProfiles.collectAsState()

    LaunchedEffect(timeLeft) {
        if (timerViewModel.stateFlow.value.timeLeft <= 0) {
            onNavigate("usernamePasswordLogin")
            timerViewModel.resetTimer()
        }
    }

    val mainBackgroundColor = if (!viewModel.stateFlow.collectAsState().value.darkMode) {
        Color.White
    } else {
        Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Main content area with weight
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(bottom = 64.dp) // Prevent content overlap
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TopAppBar(
                        title = { Text(text = topAppBarTitle) },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { showSignOutDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Sign out"
                                )
                            }
                            IconButton(onClick = { onNavigate("info") }) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        CustomCountDownTimer(timerViewModel = timerViewModel)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        if (selectedIndex in userProfilesValue.indices && !isShowingEdit) {
                            UserProfileItem(
                                userProfile = userProfilesValue[selectedIndex],
                                onEditClick = { isShowingEdit = true },
                                isEditScreen = isShowingEdit,
                                onSaveProfession = { updatedProfession ->
                                    viewModel.saveProfession(
                                        userProfilesValue[selectedIndex].imageResId,
                                        updatedProfession
                                    )
                                },
                                onInterestsSelected = { selectedInterests ->
                                    viewModel.saveInterests(
                                        userProfilesValue[selectedIndex].imageResId,
                                        selectedInterests
                                    )
                                },
                                viewModel = viewModel
                            )
                        } else {
                            EditProfile(
                                userProfiles = userProfilesValue,
                                onBackNavigate = { isShowingEdit = false },
                                isEditScreen = isShowingEdit,
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }

            CustomBottomBar(
                bottomNavigationItems = listOf(
                    BottomNavItem("Main", R.drawable.ic_back, "back"),
                    BottomNavItem("Edit", R.drawable.ic_edit, "edit"),
                    BottomNavItem("Chat", R.drawable.ic_chat, "messaging"),
                    BottomNavItem("Settings", R.drawable.ic_settings, "settings")
                ),
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    when (index) {
                        0 -> onNavigate("main")
                        1 -> onNavigate("edit")
                        2 -> onNavigate("chat")
                        3 -> onNavigate("settings")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = mainBackgroundColor
            )
        }

        if (showSignOutDialog) {
            SignOutDialog(
                viewModel = credentialsViewModel,
                onSignOut = {
                    credentialsViewModel.performSignOut()
                    showSignOutDialog = false
                    onNavigate("usernamePasswordLogin")
                },
                onDismiss = { showSignOutDialog = false }
            )
        }
    }
}