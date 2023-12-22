package com.example.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.practice.data.UserData
import com.example.practice.navigation.bottom.handler.navigateTo
import com.example.practice.navigation.bottom.navigation.BottomNavigationItems
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.screens.SplashWaitTimeMillis
import com.example.practice.screens.items.DropDownList
import com.example.practice.screens.items.UserProfileItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesLoading(
    userProfiles: List<UserData>,
    viewModel: SharedProfilesViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    username: String,
    topAppBarTitle: String
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var isShowingEdit by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Provide an initial value for selectedIndex
    val initialSelectedIndex = 0
    val selectedIndexFlow = remember { MutableStateFlow(initialSelectedIndex) }

    LaunchedEffect(isLoading) {
        delay(SplashWaitTimeMillis)
        isLoading = false
    }

    // Observe the dark mode value from the view model
    val darkMode by viewModel.darkMode.observeAsState(false)

    val mainBackgroundColor = if (darkMode) {
        MaterialTheme.colorScheme.background
    } else {
        when {
            username.lowercase().startsWith("bob") -> Color.Green
            username.lowercase().startsWith("alice") -> Color.LightGray
            username.lowercase().startsWith("eve") -> Color.Magenta
            else -> Color.Gray
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainBackgroundColor),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    if (selectedIndex in userProfiles.indices && !isShowingEdit) {
                        UserProfileItem(
                            userProfiles[selectedIndex],
                            onEditClick = {},
                            isEditScreen = isShowingEdit,
                            onSaveProfession = { updatedProfession ->
                                userProfiles[selectedIndex].profession = updatedProfession
                            },
                            viewModel = viewModel
                        )
                    } else {
                        UserProfilesList(
                            userProfiles,
                            onBackNavigate = onBack,
                            isEditScreen = isShowingEdit,
                            viewModel = viewModel
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                BottomNavigationItems(selectedIndexFlow) { navEvent ->
                    navigateTo(navEvent, onBack, onNavigate)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}



@Composable
fun UserProfileBob(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditScreen && isEditingProfession) {
            // Render text field for editing profession only on images screen
            OutlinedTextField(
                value = selectedProfession,
                onValueChange = { newProfession ->
                    selectedProfession = newProfession
                },
                label = { Text("Enter Profession") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // Toggle the visibility of the DropDownList when the arrow button is clicked
                            isDropDownListVisible = !isDropDownListVisible
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            )

            //DropDownList
            if (isDropDownListVisible) {
                DropDownList(
                    onOptionSelected = {
                        selectedProfession = it
                    },
                    expanded = isDropDownListVisible,
                    onDismissRequest = {
                        isDropDownListVisible = false
                    }
                )
            }
        } else {
            // Display user information
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between editing profession and displaying user information only on EditProfile screen
        if (isEditScreen) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isEditingProfession) {
                    Button(
                        onClick = {
                            onSaveProfession(selectedProfession)
                            isEditingProfession = false
                            viewModel.saveProfession(userProfile.imageResId, selectedProfession)
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        )
                    ) {
                        Text("Save Profession", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = {
                            isEditingProfession = !isEditingProfession
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Edit Profession", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileAlice(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditScreen && isEditingProfession) {
            // Render text field for editing profession only on images screen
            OutlinedTextField(
                value = selectedProfession,
                onValueChange = { newProfession ->
                    selectedProfession = newProfession
                },
                label = { Text("Enter Profession") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // Toggle the visibility of the DropDownList when the arrow button is clicked
                            isDropDownListVisible = !isDropDownListVisible
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            )

            //DropDownList
            if (isDropDownListVisible) {
                DropDownList(
                    onOptionSelected = {
                        selectedProfession = it
                    },
                    expanded = isDropDownListVisible,
                    onDismissRequest = {
                        isDropDownListVisible = false
                    }
                )
            }
        } else {
            // Display user information
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between editing profession and displaying user information only on EditProfile screen
        if (isEditScreen) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isEditingProfession) {
                    Button(
                        onClick = {
                            onSaveProfession(selectedProfession)
                            isEditingProfession = false
                            viewModel.saveProfession(userProfile.imageResId, selectedProfession)
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        )
                    ) {
                        Text("Save Profession", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = {
                            isEditingProfession = !isEditingProfession
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Edit Profession", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileEve(
    userProfile: UserData,
    onEditClick: () -> Unit,
    isEditScreen: Boolean,
    onSaveProfession: (String) -> Unit,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    var isEditingProfession by remember { mutableStateOf(false) }
    var selectedProfession by remember { mutableStateOf(userProfile.profession) }
    var isDropDownListVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = userProfile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    if (!isEditingProfession) {
                        onEditClick()
                    }
                }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditScreen && isEditingProfession) {
            // Render text field for editing profession only on images screen
            OutlinedTextField(
                value = selectedProfession,
                onValueChange = { newProfession ->
                    selectedProfession = newProfession
                },
                label = { Text("Enter Profession") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            // Toggle the visibility of the DropDownList when the arrow button is clicked
                            isDropDownListVisible = !isDropDownListVisible
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            )

            //DropDownList
            if (isDropDownListVisible) {
                DropDownList(
                    onOptionSelected = {
                        selectedProfession = it
                    },
                    expanded = isDropDownListVisible,
                    onDismissRequest = {
                        isDropDownListVisible = false
                    }
                )
            }
        } else {
            // Display user information
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle between editing profession and displaying user information only on EditProfile screen
        if (isEditScreen) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isEditingProfession) {
                    Button(
                        onClick = {
                            onSaveProfession(selectedProfession)
                            isEditingProfession = false
                            viewModel.saveProfession(userProfile.imageResId, selectedProfession)
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Blue
                        )
                    ) {
                        Text("Save Profession", color = Color.White)
                    }
                } else {
                    Button(
                        onClick = {
                            isEditingProfession = !isEditingProfession
                        },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Edit Profession", color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesList(
    userProfiles: List<UserData>,
    onBackNavigate: () -> Unit,
    isEditScreen: Boolean,
    viewModel: SharedProfilesViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Edit") },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onBackNavigate()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.Center
        ) {
            items(userProfiles) { userProfile ->
                UserProfileItem(
                    userProfile,
                    onEditClick = {},
                    isEditScreen,
                    onSaveProfession = { updatedProfession ->
                        userProfile.profession = updatedProfession
                        viewModel.saveProfession(userProfile.imageResId, updatedProfession)
                    },
                    viewModel = viewModel
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        // Display saved profession information
        val savedProfession = viewModel.savedProfessions.value
        if (savedProfession != null) {
            Text(
                "Saved Profession: $savedProfession",
                fontSize = 16.sp,
                color = Color.Green,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}



