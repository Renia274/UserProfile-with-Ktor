package com.example.practice.navigation.handlers

import androidx.navigation.NavHostController
import com.example.practice.navigation.graph.Navigation
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel

class NavigationHandler(
    private val navController: NavHostController,
    private val viewModel: SharedProfilesViewModel
) {

    fun navigateToRecovery() {
        navController.navigate(Navigation.Screen.Recovery.route)
    }

    fun navigateToPosts() {
        navController.navigate(Navigation.Screen.Posts.route)
    }

    fun navigateToUserProfile(username: String) {
        when {
            username.startsWith("Bob", ignoreCase = true) -> navController.navigate(Navigation.Screen.UserProfileBob.route)
            username.startsWith("Alice", ignoreCase = true) -> navController.navigate(Navigation.Screen.UserProfileAlice.route)
            username.startsWith("Eve", ignoreCase = true) -> navController.navigate(Navigation.Screen.UserProfileEve.route)
            else -> {
                null
            }
        }
    }


    fun navigateToEditProfile() {
        navController.navigate(Navigation.Screen.EditProfile.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateTo(screen: Navigation.Screen) {
        navController.navigate(screen.route)
    }

    fun navigateToSettings() {
        navController.navigate(Navigation.Screen.Settings.route)
    }

}
