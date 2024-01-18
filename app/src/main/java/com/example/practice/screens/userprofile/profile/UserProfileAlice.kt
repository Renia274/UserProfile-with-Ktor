package com.example.practice.screens.userprofile.profile

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.practice.screens.userprofile.components.EditInterestsSection
import com.example.practice.screens.userprofile.components.EditProfessionSection
import com.example.practice.screens.userprofile.components.UserProfileButtons
import com.example.practice.screens.userprofile.components.UserProfileImage
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.screens.items.CustomVerticalGrid
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.delay

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
        UserProfileImage(userProfile, onEditClick, isEditingProfession)

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
                UserProfileAliceContent(userProfile = userProfile, isEditScreen = isEditScreen)
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

@Composable
fun UserProfileAliceContent(userProfile: UserData, isEditScreen: Boolean) {
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
            CustomVerticalGrid(
                items = listOf(
                    "Item 1",
                    "Item 2",
                    "Item 3",
                    "Item 4",
                    "Item 5",
                    "Item 6"
                )
            )
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
        UserProfileEveContent(userProfile = userProfile, isEditScreen = false)
    }

}
