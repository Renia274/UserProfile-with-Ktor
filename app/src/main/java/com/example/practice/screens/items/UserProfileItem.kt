package com.example.practice.screens.items

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.UserProfileAlice
import com.example.practice.UserProfileBob
import com.example.practice.UserProfileEve
import com.example.practice.data.UserData
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel

@Composable
fun UserProfileItem(
    userProfile: UserData,
    onImageClick: () -> Unit,
    isImagesScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    when (userProfile.firstName.lowercase()) {
        "bob" -> UserProfileBob(userProfile, onImageClick, isImagesScreen, onSaveProfession, viewModel)
        "alice" -> UserProfileAlice(userProfile, onImageClick, isImagesScreen, onSaveProfession, viewModel)
        "eve" -> UserProfileEve(userProfile, onImageClick, isImagesScreen, onSaveProfession, viewModel)
        else -> UserProfileBob(userProfile, onImageClick, isImagesScreen, onSaveProfession, viewModel)
    }
}