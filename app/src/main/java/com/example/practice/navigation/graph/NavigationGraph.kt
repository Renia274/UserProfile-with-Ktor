package com.example.practice.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.R
import com.example.practice.UserProfilesList
import com.example.practice.UserProfilesLoading
import com.example.practice.data.UserData
import com.example.practice.ktor.screens.posts.PostsScreen
import com.example.practice.navigation.handlers.AuthNavigationHandler
import com.example.practice.navigation.handlers.NavigationHandler
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel

import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.screens.LoadingScreen
import com.example.practice.screens.PinLoginScreen
import com.example.practice.screens.RecoveryScreen
import com.example.practice.screens.SignUpSignInScreen
import com.example.practice.screens.SignupScreen
import com.example.practice.screens.SplashScreen
import com.example.practice.screens.UsernamePasswordLoginScreen

data class Navigation(
    val route: String,
    val auth: Auth? = null,
    val screen: Screen? = null
) {
    data class Screen(val route: String) {
        companion object {
            val Splash = Screen("splash")
            val Posts = Screen("posts")
            val UserProfileBob = Screen("userProfileBob")
            val UserProfileAlice = Screen("userProfileAlice")
            val UserProfileEve = Screen("userProfileEve")
            val Images = Screen("images")
            val Recovery = Screen("recovery")
        }
    }

    data class Auth(val route: String) {
        companion object {
            val SignUpSignIn = Auth("signupsignin")
            val Signup = Auth("signup")
            val UsernamePasswordLogin = Auth("usernamePasswordLogin")
            val PinLogin = Auth("pinLogin")
        }
    }
}

@Composable
fun NavigationApp() {

    val navController = rememberNavController()
    val credentialsViewModel = viewModel<CredentialsViewModel>()
    val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>()

    val navigationHandler = NavigationHandler(navController, sharedProfilesViewModel)
    val authNavigationHandler = AuthNavigationHandler(navController, credentialsViewModel)

    NavHost(
        navController = navController,
        startDestination = Navigation.Screen.Splash.route
    ) {
        composable(Navigation.Screen.Splash.route) {
            SplashScreen {
                authNavigationHandler.navigateToSignUpSignIn()
            }
        }
        composable(Navigation.Auth.SignUpSignIn.route) {
            SignUpSignInScreen(
                onSignUpClick = { authNavigationHandler.navigateToSignup() },
                onSignInClick = { authNavigationHandler.navigateToUsernamePasswordLogin() },
                viewModel = credentialsViewModel
            )
        }
        composable(Navigation.Screen.Recovery.route) {
            RecoveryScreen(
                navigateToLogin = { authNavigationHandler.navigateToSignUpSignIn() },
                viewModel = sharedProfilesViewModel
            )
        }
        composable(Navigation.Auth.Signup.route) {
            SignupScreen(
                onNavigateToLogin = { authNavigationHandler.navigateToUsernamePasswordLogin() },
                viewModel = credentialsViewModel
            )
        }

        composable(Navigation.Auth.UsernamePasswordLogin.route) {
            var isLoading by remember { mutableStateOf(false) }

            if (isLoading) {
                LoadingScreen()
            } else {
                UsernamePasswordLoginScreen(
                    onLoginSuccess = { username, _ ->
                        isLoading = true
                        val userProfile = when (username.lowercase()) {
                            "bob" -> UserData("Bob", "Johnson", R.drawable.bob_johnson)
                            "alice" -> UserData("Alice", "Smith", R.drawable.alice_smith)
                            "eve" -> UserData("Eve", "Brown", R.drawable.eve_brown)
                            else -> null
                        }

                        userProfile?.let {
                            sharedProfilesViewModel.userProfiles = listOf(it)
                            println("Login Successful")
                            authNavigationHandler.navigateToPinLogin()
                        } ?: run {
                            println("Invalid username")
                        }
                    },
                    onLoading = { loadingState ->
                        isLoading = loadingState
                    },
                    onNavigateToRecovery = {
                        navigationHandler.navigateToRecovery()
                    },
                    onBack = {
                        navigationHandler.navigateBack()
                    },
                    viewModel = credentialsViewModel
                )
            }
        }

        composable(Navigation.Auth.PinLogin.route) {
            PinLoginScreen(
                onLoginSuccess = { userProfile ->
                    sharedProfilesViewModel.userProfiles = listOf(userProfile)
                    navigationHandler.navigateToUserProfile(userProfile.firstName)
                },
                onNavigate = { screen: Navigation.Screen -> navigationHandler.navigateTo(screen) },
                onPostNavigate = { navigationHandler.navigateToPosts() }
            )
        }

        composable(Navigation.Screen.Posts.route) {
            PostsScreen(onNavigate = {
                authNavigationHandler.navigateToSignUpSignIn()
            })
        }

        composable(Navigation.Screen.UserProfileBob.route) {
            UserProfilesLoading(
                userProfiles = sharedProfilesViewModel.userProfiles,
                viewModel = sharedProfilesViewModel,
                onBack = {
                    authNavigationHandler.navigateToSignUpSignIn()
                },
                onNavigate = { destination ->
                    when (destination) {
                        "images" -> {
                            navigationHandler.navigateToImages()
                        }
                        "back" -> {
                            navigationHandler.navigateBack()
                        }
                    }
                },
                username = "Bob"
            )
        }

        composable(Navigation.Screen.UserProfileAlice.route) {
            UserProfilesLoading(
                userProfiles = sharedProfilesViewModel.userProfiles,
                viewModel = sharedProfilesViewModel,
                onBack = {
                    authNavigationHandler.navigateToSignUpSignIn()
                },
                onNavigate = { destination ->
                    when (destination) {
                        "images" -> {
                            navigationHandler.navigateToImages()
                        }
                        "back" -> {
                            navigationHandler.navigateBack()
                        }
                    }
                },
                username = "Alice"
            )
        }

        composable(Navigation.Screen.UserProfileEve.route) {
            UserProfilesLoading(
                userProfiles = sharedProfilesViewModel.userProfiles,
                viewModel = sharedProfilesViewModel,
                onBack = {
                    authNavigationHandler.navigateToSignUpSignIn()
                },
                onNavigate = { destination ->
                    when (destination) {
                        "images" -> {
                            navigationHandler.navigateToImages()
                        }
                        "back" -> {
                            navigationHandler.navigateBack()
                        }
                    }
                },
                username = "Eve"
            )
        }

        composable(Navigation.Screen.Images.route) {
            UserProfilesList(
                userProfiles = sharedProfilesViewModel.userProfiles,
                onBackNavigate = {
                    navigationHandler.navigateBack()
                },
                isImagesScreen = true,
                viewModel = sharedProfilesViewModel
            )
        }
    }
}
