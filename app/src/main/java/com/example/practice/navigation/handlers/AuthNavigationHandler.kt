package com.example.practice.navigation.handlers

import androidx.navigation.NavHostController
import com.example.practice.navigation.graph.Navigation
import com.example.practice.profiles.viewmodel.UserProfileViewModel

class AuthNavigationHandler(private val navController: NavHostController, private val viewModel: UserProfileViewModel) {

    fun navigateToSignUpSignIn() {
        navController.popBackStack()
        navController.navigate(Navigation.Auth.SignUpSignIn.route)
    }


    fun navigateToSignup() {
        navController.navigate(Navigation.Auth.Signup.route)
    }

    fun navigateToUsernamePasswordLogin() {
        navController.navigate(Navigation.Auth.UsernamePasswordLogin.route)
    }

    fun navigateToPinLogin() {
        navController.navigate(Navigation.Auth.PinLogin.route)
    }
}
