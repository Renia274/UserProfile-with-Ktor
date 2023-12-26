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
import com.example.practice.screens.SecurityCodeScreen
import com.example.practice.screens.SettingsScreen
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
            val EditProfile = Screen("edit")
            val Recovery = Screen("recovery")
            val Settings = Screen("settings")
        }
    }

    data class Auth(val route: String) {
        companion object {
            val SignUpSignIn = Auth("signupsignin")
            val Signup = Auth("signup")
            val UsernamePasswordLogin = Auth("usernamePasswordLogin")
            val PinLogin = Auth("pinLogin")
            val SecurityCode = Auth("securityCode")
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
                sharedViewModel = sharedProfilesViewModel,
                credentialsViewModel = credentialsViewModel
            )
        }

        composable(Navigation.Auth.UsernamePasswordLogin.route) {
            var isLoading by remember { mutableStateOf(false) }

            if (isLoading) {
                LoadingScreen()
            } else {
                val securityCodeAvailable = credentialsViewModel.securityCode.value != null

                if (securityCodeAvailable) {
                    authNavigationHandler.navigateToPinLogin()
                } else {
                    UsernamePasswordLoginScreen(
                        onLoginSuccess = { enteredUsername, _, _, _ ->
                            isLoading = true
                            val userProfile = when {
                                enteredUsername.lowercase().startsWith("bob") -> UserData(
                                    "Bob",
                                    "Johnson",
                                    R.drawable.bob_johnson
                                )
                                enteredUsername.lowercase().startsWith("alice") -> UserData(
                                    "Alice",
                                    "Smith",
                                    R.drawable.alice_smith
                                )
                                enteredUsername.lowercase().startsWith("eve") -> UserData(
                                    "Eve",
                                    "Brown",
                                    R.drawable.eve_brown
                                )
                                else -> null
                            }

                            userProfile?.let {
                                sharedProfilesViewModel.userProfiles = listOf(it)
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
        }

        composable(Navigation.Screen.Settings.route) {
            SettingsScreen(
                sharedViewModel = sharedProfilesViewModel,
                credentialsViewModel = credentialsViewModel,
                onNavigate = { destination ->
                    when (destination) {
                        "back" -> navigationHandler.navigateBack()
                        "securityCode" -> {
                            authNavigationHandler.navigateToSecurityCode()
                        }
                        "usernamePasswordLogin" -> authNavigationHandler.navigateToUsernamePasswordLogin()
                    }
                }
            ) { newUsername, newPassword ->
                credentialsViewModel.setEnteredCredentials(
                    username = newUsername,
                    password = newPassword
                )
            }
        }

        composable(Navigation.Auth.SecurityCode.route) {
            SecurityCodeScreen(
                viewModel = credentialsViewModel,
                onNavigate = { destination ->
                    when (destination) {
                        "usernamePasswordLogin" -> authNavigationHandler.navigateToUsernamePasswordLogin()
                        "back" -> navigationHandler.navigateBack()
                    }
                },
                securityCode = credentialsViewModel.securityCode.value ?: ""
            )
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
                authNavigationHandler.navigateToPinLogin()
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
                        "edit" -> {
                            navigationHandler.navigateToEditProfile()
                        }
                        "back" -> {
                            navigationHandler.navigateBack()
                        }
                        "settings" -> {
                            navigationHandler.navigateToSettings()
                        }
                    }
                },
                username = "Bob",
                topAppBarTitle = "Bob's Profile"
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
                        "edit" -> {
                            navigationHandler.navigateToEditProfile()
                        }
                        "back" -> {
                            navigationHandler.navigateBack()
                        }
                        "settings" -> {
                            navigationHandler.navigateToSettings()
                        }
                    }
                },
                username = "Alice",
                topAppBarTitle = "Alice's Profile"
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
                        "edit" -> {
                            navigationHandler.navigateToEditProfile()
                        }
                        "back" -> {
                            navigationHandler.navigateBack()
                        }
                        "settings" -> {
                            navigationHandler.navigateToSettings()
                        }
                    }
                },
                username = "Eve",
                topAppBarTitle = "Eve's Profile"
            )
        }

        composable(Navigation.Screen.EditProfile.route) {
            UserProfilesList(
                userProfiles = sharedProfilesViewModel.userProfiles,
                onBackNavigate = {
                    navigationHandler.navigateBack()
                },
                isEditScreen = true,
                viewModel = sharedProfilesViewModel
            )
        }
    }
}