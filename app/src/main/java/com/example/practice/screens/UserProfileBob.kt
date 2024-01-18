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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
fun UserProfileImage(userProfile: UserData, onEditClick: () -> Unit, isEditingProfession: Boolean) {
    val imageSize by animateDpAsState(
        targetValue = if (isEditingProfession) 150.dp else 200.dp,
        animationSpec = tween(durationMillis = 500),
        label = "imageSizeAnimation"
    )

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
}

@Composable
fun EditProfessionSection(
    selectedProfession: String,
    onProfessionValueChange: (String) -> Unit,
    isDropDownListVisible: Boolean,
    onDropDownClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = selectedProfession,
            onValueChange = { newProfession ->
                onProfessionValueChange(newProfession)
            },
            label = { Text("Enter Profession") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                IconButton(onClick = {
                    onDropDownClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        )

        // Show the DropDownList when isDropDownListVisible is true
        if (isDropDownListVisible) {
            DropDownList(
                onOptionSelected = { selectedOption ->
                    // Handle the selected option
                    onProfessionValueChange(selectedOption)
                },
                expanded = isDropDownListVisible,
                onDismissRequest = {
                    // Close the DropDownList when dismissed
                    onDropDownClick()
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun EditInterestsSection(
    selectedInterests: List<String>,
    onInterestsValueChange: (List<String>) -> Unit,
    isInterestsDropDownListVisible: Boolean,
    onInterestsDropDownClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = selectedInterests.joinToString(", "),
            onValueChange = { newInterests ->
                onInterestsValueChange(newInterests.split(", ").map { it.trim() })
            },
            label = { Text("Enter Interests") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                IconButton(onClick = {
                    onInterestsDropDownClick()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }
        )

        // Show the InterestsDropDownList when isInterestsDropDownListVisible is true
        if (isInterestsDropDownListVisible) {
            InterestsDropDownList(
                onInterestsSelected = { selectedOptions ->
                    // Handle the selected options
                    onInterestsValueChange(selectedOptions)
                },
                selectedInterests = selectedInterests,
                expanded = isInterestsDropDownListVisible,
                onDismissRequest = {
                    // Close the InterestsDropDownList when dismissed
                    onInterestsDropDownClick()
                }
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun UserProfileButtons(
    isEditingProfession: Boolean,
    onSaveProfession: () -> Unit,
    onEditProfessionClick: () -> Unit,
    isEditingInterests: Boolean,
    onSaveInterests: () -> Unit,
    onEditInterestsClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isEditingProfession) {
            Button(
                onClick = {
                    onSaveProfession()
                    onEditProfessionClick()
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text("Save Profession", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    onEditProfessionClick()
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
                    onSaveInterests()
                   onEditInterestsClick()
                }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text("Save Interests", color = Color.White)
            }
        } else {
            Button(
                onClick = {
                    onEditInterestsClick()
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
        UserProfileImage(userProfile, onEditClick, isEditingProfession)

        when {
            isEditScreen && isEditingProfession -> {
                EditProfessionSection(selectedProfession, { newProfession -> selectedProfession = newProfession }, isDropDownListVisible, { isDropDownListVisible = !isDropDownListVisible })
            }
            isEditingInterests -> {
                EditInterestsSection(selectedInterests, { newInterests -> selectedInterests = newInterests }, isInterestsDropDownListVisible, { isInterestsDropDownListVisible = !isInterestsDropDownListVisible })
            }
            else -> {
                UserProfileBobContent(userProfile = userProfile, isEditScreen = isEditScreen)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditScreen) {
            UserProfileButtons(isEditingProfession, { onSaveProfession(selectedProfession) }, { isEditingProfession = !isEditingProfession }, isEditingInterests, { onInterestsSelected(selectedInterests) }, { isEditingInterests = !isEditingInterests })
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