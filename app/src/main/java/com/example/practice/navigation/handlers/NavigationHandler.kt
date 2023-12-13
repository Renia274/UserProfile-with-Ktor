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
        when (username) {
            "Bob" -> navController.navigate(Navigation.Screen.UserProfileBob.route)
            "Alice" -> navController.navigate(Navigation.Screen.UserProfileAlice.route)
            "Eve" -> navController.navigate(Navigation.Screen.UserProfileEve.route)
        }
    }

    fun navigateToImages() {
        navController.navigate(Navigation.Screen.Images.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateTo(screen: Navigation.Screen) {
        navController.navigate(screen.route)
    }

}
