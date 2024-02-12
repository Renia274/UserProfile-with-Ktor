package com.example.practice.navigation.handlers

import androidx.navigation.NavHostController
import com.example.practice.navigation.graph.Navigation

class NavigationHandler(
    private val navController: NavHostController
) {

    fun navigateToRecovery() {
        navController.navigate(Navigation.Screen.Recovery.route)
    }

    fun navigateToPosts() {
        navController.navigate(Navigation.Screen.Posts.route)
    }

    fun navigateToUserProfile(username: String) {
        when {
            username.startsWith(
                "Bob",
                ignoreCase = true
            ) -> navController.navigate(Navigation.Screen.UserProfileBob.route)

            username.startsWith(
                "Alice",
                ignoreCase = true
            ) -> navController.navigate(Navigation.Screen.UserProfileAlice.route)

            username.startsWith(
                "Eve",
                ignoreCase = true
            ) -> navController.navigate(Navigation.Screen.UserProfileEve.route)

            else -> {
                null
            }
        }
    }

    fun navigateToInfoScreen() {
        navController.navigate(Navigation.Screen.InfoScreen.route)
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

    fun navigateToMessagingScreen() {
        navController.navigate(Navigation.Screen.MessagingScreen.route)
    }


    fun navigateToSettings() {
        navController.navigate(Navigation.Screen.Settings.route)
    }


    fun navigateToPermissionScreen() {
        navController.navigate(Navigation.Screen.PermissionScreen.route)
    }


}
