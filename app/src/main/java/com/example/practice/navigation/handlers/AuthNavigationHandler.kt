package com.example.practice.navigation.handlers

import androidx.navigation.NavHostController
import com.example.practice.navigation.graph.Navigation

class AuthNavigationHandler(private val navController: NavHostController) {

    fun navigateToSignUpSignIn() {
        navController.popBackStack()
        navController.navigate(Navigation.Auth.SignUpSignIn.route)
    }


    fun navigateToSignup() {
        navController.navigate(Navigation.Auth.Signup.route)
    }

    fun navigateToOtp() {
        navController.navigate(Navigation.Auth.OtpScreen.route)
    }

    fun navigateToSecurityCode(){
        navController.navigate(Navigation.Auth.SecurityCode.route)
    }

    fun navigateToUsernamePasswordLogin() {
        navController.navigate(Navigation.Auth.UsernamePasswordLogin.route)
    }


    fun navigateToPinSetup() {
        navController.navigate(Navigation.Auth.PinSetup.route)
    }

    fun navigateToPinLogin() {
        navController.navigate(Navigation.Auth.PinLogin.route)
    }


}
