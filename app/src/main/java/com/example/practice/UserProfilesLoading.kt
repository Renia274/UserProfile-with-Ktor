package com.example.practice


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.example.practice.navigation.bottom.handler.navigateTo
import com.example.practice.navigation.bottom.navigation.BottomNavigationItems
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import com.example.practice.screens.userprofile.editprofile.EditProfile
import com.example.practice.screens.userprofile.profile.components.CustomCountDownTimer
import com.example.practice.screens.userprofile.profile.components.SignOutDialog
import com.example.practice.screens.userprofile.profile.components.UserProfileItem
import kotlinx.coroutines.flow.MutableStateFlow
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
    // State for showing the edit profile screen
    var isShowingEdit by remember { mutableStateOf(false) }

    var showSignOutDialog by remember { mutableStateOf(false) }

    val initialSelectedIndex = 0
    val selectedIndexFlow = remember { MutableStateFlow(initialSelectedIndex) }

    val timerState by timerViewModel.stateFlow.collectAsState()
    val timeLeft = timerState.timeLeft

    // Choose between UserProfile and EditProfile based on edit mode
    val selectedIndex by selectedIndexFlow.collectAsState()
    val userProfilesValue by userProfiles.collectAsState()

    // Check if the timer has run out and trigger navigation
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

    val imageSize = if (isShowingEdit) 150.dp else 200.dp // Define imageSize here

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainBackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = topAppBarTitle,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    // Triggers the sign-out dialog
                    IconButton(onClick = {
                        showSignOutDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }

                    // Navigates to the InfoScreen
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

                if (selectedIndex in userProfilesValue.indices && !isShowingEdit) {
                    UserProfileItem(
                        userProfile = userProfilesValue[selectedIndex],
                        onEditClick = {
                            isShowingEdit = false
                        },
                        isEditScreen = isShowingEdit,
                        onSaveProfession = { updatedProfession ->
                            val userProfile = userProfilesValue[selectedIndex]
                            viewModel.saveProfession(userProfile.imageResId, updatedProfession)
                        },
                        onInterestsSelected = { selectedInterests ->
                            val userProfile = userProfilesValue[selectedIndex]
                            viewModel.saveInterests(userProfile.imageResId, selectedInterests)
                        },
                        viewModel = viewModel
                    )
                } else {
                    EditProfile(
                        userProfilesValue,
                        onBackNavigate = onBack,
                        isEditScreen = isShowingEdit,
                        viewModel = viewModel
                    )
                }
            }

            if (showSignOutDialog) {
                SignOutDialog(viewModel = credentialsViewModel, onSignOut = {
                    credentialsViewModel.performSignOut()
                    showSignOutDialog = false // Dismiss the dialog after sign-out
                    onNavigate("usernamePasswordLogin")
                }, onDismiss = {
                    showSignOutDialog = false // Dismiss the dialog if canceled
                })
            }

            Spacer(modifier = Modifier.weight(1f))

            BottomNavigationItems(selectedIndexFlow) { navEvent ->
                navigateTo(navEvent, onBack, onNavigate)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
