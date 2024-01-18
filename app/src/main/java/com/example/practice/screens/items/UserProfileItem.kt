package com.example.practice.screens.items

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.screens.userprofile.profile.UserProfileAlice
import com.example.practice.screens.userprofile.profile.UserProfileBob
import com.example.practice.screens.userprofile.profile.UserProfileEve
import com.example.practice.data.UserData
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel

@Composable
fun UserProfileItem(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    onInterestsSelected: (List<String>) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    // Extracting username from the user profile
    val username = userProfile.firstName.lowercase()

    // Displaying different user profiles based on the username prefix
    when {
        username.startsWith("bob") -> {
            UserProfileBob(
                userProfile,
                onEditClick,
                isEditScreen,
                onSaveProfession,
                onInterestsSelected,
                viewModel
            )
        }

        username.startsWith("alice") -> {
            UserProfileAlice(
                userProfile,
                onEditClick,
                isEditScreen,
                onSaveProfession,
                onInterestsSelected,
                viewModel
            )
        }

        username.startsWith("eve") -> {
            UserProfileEve(
                userProfile,
                onEditClick,
                isEditScreen,
                onSaveProfession,
                onInterestsSelected,
                viewModel
            )
        }
    }
}