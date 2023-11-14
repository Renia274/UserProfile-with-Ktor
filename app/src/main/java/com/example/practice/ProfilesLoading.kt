package com.example.practice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.practice.ui.theme.PracticeTheme

data class ProfileData(val firstName: String, val lastName: String, val imageResId: Int) {
    fun contains(query: String): Boolean {
        val fullName = "$firstName $lastName"
        return fullName.contains(query, ignoreCase = true)
    }
}

data class BottomNavItem(val label: String, val iconResId: Int, val route: String)

@Composable
fun BottomBar(
    bottomNavigationItems: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primary // Default color
) {
    BottomNavigation(
        modifier = Modifier
            .background(backgroundColor)
    ) {
        bottomNavigationItems.forEachIndexed { index, item ->
            BottomNavigationItem(
                selected = index == selectedIndex,
                onClick = {
                    onItemSelected(index)
                },
                icon = {
                    Image(
                        painter = painterResource(id = item.iconResId),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp) // Set the desired size here
                    )
                },
                label = { Text(item.label) },
                modifier = Modifier.background(backgroundColor)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesLoading() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isDrawerOpen by remember { mutableStateOf(false) }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val profiles = listOf(
        ProfileData("Bob", "Johnson", R.drawable.bob_johnson),
        ProfileData("Eve", "Brown", R.drawable.eve_brown),
        ProfileData("Alice", "Smith", R.drawable.alice_smith)
    )

    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Profiles") },
            navigationIcon = {
                IconButton(onClick = {
                    isDrawerOpen = !isDrawerOpen
                }) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }
            },
            actions = {
                IconButton(onClick = {
                    isSearchBarVisible = !isSearchBarVisible // Toggle the search bar
                    // Clear search query when closing the search bar
                    if (!isSearchBarVisible) {
                        searchQuery = ""
                    }
                }) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            }
        )

        // Display the SearchBar only if isSearchBarVisible is true
        if (isSearchBarVisible) {
            SearchBar { query ->
                searchQuery = query
                // Reset selectedIndex when a new search query is entered
                selectedIndex = 0
            }
        }

        // Filter profiles based on the search query
        val filteredProfiles = profiles.filter { it.contains(searchQuery) }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDrawerOpen) {
                    DrawerContent(
                        profiles = profiles,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index ->
                            selectedIndex = index
                            isDrawerOpen = false
                        }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Display the first matching profile in the filtered list
                if (selectedIndex in filteredProfiles.indices) {
                    Profile(filteredProfiles[selectedIndex], navController)
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Expandable space

            // BottomBar without additional padding
            BottomBar(
                bottomNavigationItems = listOf(
                    BottomNavItem("Back", R.drawable.ic__left, "back"),
                    BottomNavItem("Next", R.drawable.ic___right, "next")
                ),
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    when (index) {
                        0 -> {
                            // Navigate back to the previous profile
                            if (selectedIndex > 0) {
                                selectedIndex--
                            }
                        }
                        1 -> {
                            // Navigate to the next profile
                            if (selectedIndex < filteredProfiles.size - 1) {
                                selectedIndex++
                            }
                        }
                    }
                },
                backgroundColor = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}



@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = {
            searchText = it
            onSearch(it)
        },
        placeholder = { Text("Search profiles") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun DrawerContent(
    profiles: List<ProfileData>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val overrideFontPadding = PlatformTextStyle(includeFontPadding = false)
    val h6 = TextStyle(
        fontSize = 14.sp,
        platformStyle = overrideFontPadding
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Drawer header
        Text(
            text = "Person Profiles",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            style = h6
        )

        // Drawer items
        profiles.forEachIndexed { index, profile ->
            DrawerItem(
                isSelected = index == selectedIndex,
                onClick = { onItemSelected(index) },
                profile = profile
            )
        }
    }
}

@Composable
fun DrawerItem(isSelected: Boolean, onClick: () -> Unit, profile: ProfileData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = profile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text for the drawer item-person profile
        val name = "${profile.firstName} ${profile.lastName}"
        Text(text = name, color = if (isSelected) Color.Red else Color.Gray)
    }
}

@Composable
fun Profile(profile: ProfileData, navController: NavHostController) {
    val overrideFontPadding = PlatformTextStyle(includeFontPadding = false)
    val h6 = TextStyle(
        fontSize = 14.sp,
        platformStyle = overrideFontPadding
    )
    val subtitle1 = TextStyle(
        fontSize = 15.sp,
        platformStyle = overrideFontPadding
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = profile.imageResId),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondary)
                .clickable {
                    navController.navigate("details/${profile.imageResId}")
                }
        )

        // First Name
        Text(
            text = "First Name: ${profile.firstName}",
            style = h6,
            color = Color.Red,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Last Name
        Text(
            text = "Last Name: ${profile.lastName}",
            style = subtitle1,
            color = Color.Red,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview
@Composable
fun ProfilesLoadingPreview() {
    PracticeTheme {
        ProfilesLoading()
    }
}
