package com.example.practice.navigation.bottom.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.practice.R
import com.example.practice.navigation.bottom.handler.BottomNavigationHandler
import com.example.practice.navigation.bottom.navItems.BottomNavItem
import com.example.practice.screens.userprofile.profile.components.CustomBottomBar
import kotlinx.coroutines.flow.Flow


@Composable
fun BottomNavigationItems(
    selectedIndex: Flow<Int>,
    onNavigate: (BottomNavigationHandler) -> Unit
) {
    val selectedIndexChanged by selectedIndex.collectAsState(initial = 0)

    CustomBottomBar(
        bottomNavigationItems = listOf(
            BottomNavItem("Main", R.drawable.ic_back, "back"),
            BottomNavItem("Edit", R.drawable.ic_edit, "edit"),
            BottomNavItem("Chat", R.drawable.ic_chat, "messaging"),
            BottomNavItem("Settings", R.drawable.ic_settings, "settings")
        ),
        selectedIndex = selectedIndexChanged,
        onItemSelected = { index ->
            when (index) {
                0 -> onNavigate(BottomNavigationHandler.Back)
                1 -> onNavigate(BottomNavigationHandler.EditProfile)
                2-> onNavigate(BottomNavigationHandler.Messaging)
                3 -> onNavigate(BottomNavigationHandler.Settings)

            }
        },
        backgroundColor = Color.Yellow
    )
}
