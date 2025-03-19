package com.example.practice.screens.userprofile.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.data.UserData
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.screens.userprofile.profile.components.CustomVerticalGrid
import com.example.practice.screens.userprofile.profile.components.EditInterestsSection
import com.example.practice.screens.userprofile.profile.components.EditProfessionSection
import com.example.practice.screens.userprofile.profile.components.UserProfileButtons
import com.example.practice.screens.userprofile.profile.components.UserProfileImage
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.delay
import com.example.practice.navigation.bottom.navItems.BottomNavItem
import com.example.practice.screens.userprofile.profile.components.CustomBottomBar

@Composable
fun UserProfileAlice(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    onInterestsSelected: (List<String>) -> Unit,
    viewModel: SharedProfilesViewModel
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    var isEditingInterests by remember { mutableStateOf(false) }
    var selectedInterests by remember { mutableStateOf(userProfile.interests) }
    var isInterestsDropDownListVisible by remember { mutableStateOf(false) }

    var isDelayApplied by remember { mutableStateOf(false) }
    var selectedBottomNavIndex by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        if (!isDelayApplied) {
            delay(2000)
            isDelayApplied = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Bottom padding to ensure content doesn't overlap with bottom bar
                .padding(bottom = 65.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserProfileImage(userProfile, onEditClick, isEditingProfession, isEditingInterests)

            when {
                isEditScreen && isEditingProfession -> {
                    EditProfessionSection(
                        selectedProfession,
                        { newProfession -> selectedProfession = newProfession },
                        isDropDownListVisible,
                        { isDropDownListVisible = !isDropDownListVisible })
                }

                isEditingInterests -> {
                    EditInterestsSection(
                        selectedInterests,
                        { newInterests -> selectedInterests = newInterests },
                        isInterestsDropDownListVisible,
                        { isInterestsDropDownListVisible = !isInterestsDropDownListVisible })
                }

                else -> {
                    UserProfileAliceContent(
                        userProfile = userProfile,
                        isEditScreen = isEditScreen,
                        darkModeState = viewModel.stateFlow.collectAsState().value.darkMode
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditScreen) {
                UserProfileButtons(
                    isEditingProfession,
                    { onSaveProfession(selectedProfession) },
                    { isEditingProfession = !isEditingProfession },
                    isEditingInterests,
                    { onInterestsSelected(selectedInterests) },
                    { isEditingInterests = !isEditingInterests })
            }
        }


    }
}

@Composable
fun UserProfileAliceContent(userProfile: UserData, isEditScreen: Boolean, darkModeState: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
            // Grid should take available space but not need scrolling
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CustomVerticalGrid(
                    items = listOf(
                        "Item 1" to R.drawable.alice_smith,
                        "Item 2" to R.drawable.alice_smith,
                        "Item 3" to R.drawable.alice_smith,
                        "Item 4" to R.drawable.alice_smith,
                        "Item 5" to R.drawable.alice_smith,
                        "Item 6" to R.drawable.alice_smith
                    ),
                    darkModeState = darkModeState
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable

fun UserProfileAliceContentPreview() {
    val userProfile = UserData(
        imageResId = R.drawable.bob_johnson,
        firstName = "Alice",
        lastName = "Smith",
        profession = "Software Engineer",
        interests = listOf("Programming", "Music")
    )

    PracticeTheme {
        UserProfileEveContent(userProfile = userProfile, isEditScreen = false, darkModeState = true)
    }

}
