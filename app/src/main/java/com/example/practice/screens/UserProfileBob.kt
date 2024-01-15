package com.example.practice.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice.R
import com.example.practice.data.UserData
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.screens.items.CustomVerticalGrid
import com.example.practice.screens.items.DropDownList
import com.example.practice.screens.items.InterestsDropDownList
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.delay

@Composable
fun UserProfileBob(
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
        Image(
            painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                }
        )

        when {
            isEditScreen && isEditingProfession -> {
                OutlinedTextField(
                    value = selectedProfession,
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
                    }
                )

                if (isDropDownListVisible) {
                    DropDownList(
                        onOptionSelected = { selectedProfession = it },
                        expanded = isDropDownListVisible,
                        onDismissRequest = { isDropDownListVisible = false }
                    )
                }
            }

            isEditingInterests -> {
                InterestsDropDownList(
                    onInterestsSelected = { selectedInterests = it },
                    selectedInterests = selectedInterests,
                    expanded = isInterestsDropDownListVisible,
                    onDismissRequest = { isInterestsDropDownListVisible = false }
                )

                OutlinedTextField(
                    value = selectedInterests.joinToString(", "),
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
                    }
                )
            }

            else -> {
                UserProfileBobContent(userProfile = userProfile, isEditScreen = isEditScreen)
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

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun UserProfileBobContent(userProfile: UserData, isEditScreen: Boolean) {
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

fun UserProfileBobContentPreview() {
    val userProfile = UserData(
        imageResId = R.drawable.bob_johnson,
        firstName = "Bob",
        lastName = "Johnson",
        profession = "Software Engineer",
        interests = listOf("Programming", "Reading", "Music")
    )

    PracticeTheme {
        UserProfileEveContent(userProfile = userProfile, isEditScreen = false)
    }

}
