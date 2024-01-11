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
import com.example.practice.navigation.handlers.AuthNavigationHandler
import com.example.practice.navigation.handlers.NavigationHandler
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewModel
import com.example.practice.profiles.viewmodel.permissions.PermissionStateViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import com.example.practice.screens.InfoScreen
import com.example.practice.screens.LoadingScreen
import com.example.practice.screens.OtpScreen
import com.example.practice.screens.PermissionScreen
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
            val InfoScreen = Screen("info")
            val PermissionScreen=Screen("permissions")
        }
    }

    data class Auth(val route: String) {
        companion object {
            val SignUpSignIn = Auth("signupsignin")
            val Signup = Auth("signup")
            val UsernamePasswordLogin = Auth("usernamePasswordLogin")
            val PinLogin = Auth("pinLogin")
            val SecurityCode = Auth("securityCode")
            val OtpScreen = Auth("otpScreen")
        }
    }
}


@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    val credentialsViewModel = viewModel<CredentialsViewModel>()
    val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>()
    val timerViewModel = viewModel<TimerViewModel>()
    val otpViewModel = viewModel<FirebaseOtpViewModel>()
    val permissionStateViewModel = viewModel<PermissionStateViewModel>()


    val navigationHandler = NavigationHandler(navController, sharedProfilesViewModel)
    val authNavigationHandler = AuthNavigationHandler(navController, credentialsViewModel)

    NavHost(
        navController = navController,
        startDestination = Navigation.Screen.Splash.route
    ) {
        composable("splash") {
            SplashScreen {
                authNavigationHandler.navigateToSignUpSignIn()
            }
        }

        composable("signupsignin") {
            SignUpSignInScreen(
                onSignUpClick = { authNavigationHandler.navigateToSignup() },
                onSignInClick = { authNavigationHandler.navigateToUsernamePasswordLogin() },
                viewModel = credentialsViewModel
            )
        }

        composable("recovery") {
            RecoveryScreen(
                navigateToLogin = { authNavigationHandler.navigateToSignUpSignIn() },
                viewModel = sharedProfilesViewModel,
                onNavigateBack = { navigationHandler.navigateBack() }
            )
        }

        composable("signup") {
            SignupScreen(
                onNavigateToLogin = { authNavigationHandler.navigateToUsernamePasswordLogin() },
                sharedViewModel = sharedProfilesViewModel,
                credentialsViewModel = credentialsViewModel,
                onNavigate = { authNavigationHandler.navigateToOtp() }
            )
        }


        composable(Navigation.Auth.OtpScreen.route) {
            OtpScreen(
                onNavigate = {
                    authNavigationHandler.navigateToUsernamePasswordLogin()
                },
                onBackPressed = { navigationHandler.navigateBack() },
                viewModel = sharedProfilesViewModel,
                otpViewModel = otpViewModel
            )
        }

        composable("usernamePasswordLogin") {
            var isLoading by remember { mutableStateOf(false) }

            if (isLoading) {
                LoadingScreen()
            } else {
                val securityCodeAvailable = credentialsViewModel.credentialsState.value.securityCode != null


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
        composable("settings") {
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

                        "permissions"->navigationHandler.navigateToPermissionScreen()
                    }
                }
            ) { newUsername, newPassword ->
                credentialsViewModel.setEnteredCredentials(
                    username = newUsername,
                    password = newPassword
                )
            }
        }

        composable("permissions") {
            PermissionScreen(
                permissionStateViewModel = permissionStateViewModel,
                onBackButtonClick = { navigationHandler.navigateBack() }
            )
        }


        composable("pinLogin") {
            PinLoginScreen(
                onLoginSuccess = { userProfile ->
                    sharedProfilesViewModel.userProfiles = listOf(userProfile)
                    navigationHandler.navigateToUserProfile(userProfile.firstName)
                },
                onNavigate = { screen: Navigation.Screen -> navigationHandler.navigateTo(screen) },
                onPostNavigate = { navigationHandler.navigateToPosts() }
            )
        }

        composable("securityCode") {
            SecurityCodeScreen(
                viewModel = credentialsViewModel,
                onNavigate = { destination ->
                    when (destination) {
                        "usernamePasswordLogin" -> authNavigationHandler.navigateToUsernamePasswordLogin()
                        "back" -> navigationHandler.navigateBack()
                        "pinLogin" -> authNavigationHandler.navigateToPinLogin()
                    }
                },
                securityCode = credentialsViewModel.credentialsState.value.securityCode ?: "",
            )
        }


        composable("userProfileBob") { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username") ?: "Bob"
            UserProfilesLoading(
                userProfiles = sharedProfilesViewModel.userProfiles,
                viewModel = sharedProfilesViewModel,
                timerViewModel = timerViewModel,
                credentialsViewModel = credentialsViewModel,
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

                        "info" -> {
                            navigationHandler.navigateToInfoScreen()
                        }

                        "usernamePasswordLogin" -> {
                            authNavigationHandler.navigateToUsernamePasswordLogin()
                        }
                    }
                },
                username = username,
                topAppBarTitle = "$username's Profile"
            )
        }

        composable("userProfileAlice") { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username") ?: "Alice"
            UserProfilesLoading(
                userProfiles = sharedProfilesViewModel.userProfiles,
                viewModel = sharedProfilesViewModel,
                timerViewModel = timerViewModel,
                credentialsViewModel = credentialsViewModel,
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

                        "info" -> {
                            navigationHandler.navigateToInfoScreen()
                        }

                        "usernamePasswordLogin" -> {
                            authNavigationHandler.navigateToUsernamePasswordLogin()
                        }
                    }
                },
                username = username,
                topAppBarTitle = "$username's Profile"
            )
        }

        composable("userProfileEve") { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username") ?: "Eve"
            UserProfilesLoading(
                userProfiles = sharedProfilesViewModel.userProfiles,
                viewModel = sharedProfilesViewModel,
                timerViewModel = timerViewModel,
                credentialsViewModel = credentialsViewModel,
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

                        "info" -> {
                            navigationHandler.navigateToInfoScreen()
                        }

                        "usernamePasswordLogin" -> {
                            authNavigationHandler.navigateToUsernamePasswordLogin()
                        }
                    }
                },
                username = username,
                topAppBarTitle = "$username's Profile"
            )
        }

        composable("info") {
            InfoScreen(onNavigateBack = { navigationHandler.navigateBack() })

        }

        composable("edit") {
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

