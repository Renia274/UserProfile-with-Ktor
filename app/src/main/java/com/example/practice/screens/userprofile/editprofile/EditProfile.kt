package com.example.practice.screens.userprofile.editprofile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practice.data.UserData
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.screens.items.UserProfileItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfile(
    userProfiles: List<UserData>,
    onBackNavigate: () -> Unit,
    isEditScreen: Boolean,
    viewModel: SharedProfilesViewModel
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

