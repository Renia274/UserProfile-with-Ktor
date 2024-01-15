package com.example.practice.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.screens.EditProfile
import com.example.practice.R
import com.example.practice.UserProfilesLoading
import com.example.practice.data.UserData
import com.example.practice.ktor.screens.posts.PostsScreen
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
            val PermissionScreen = Screen("permissions")
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

    val navigationHandler = NavigationHandler(navController)
    val authNavigationHandler = AuthNavigationHandler(navController)

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

            )
        }


        composable(Navigation.Auth.Signup.route) {
            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )
            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            SignupScreen(
                onNavigateToLogin = { authNavigationHandler.navigateToUsernamePasswordLogin() },
                sharedViewModel = sharedProfilesViewModel,
                credentialsViewModel = credentialsViewModel,
                onNavigate = { authNavigationHandler.navigateToOtp() }
            )
        }


        composable(Navigation.Auth.OtpScreen.route) {

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val otpViewModel = viewModel<FirebaseOtpViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


            OtpScreen(
                onNavigate = {
                    authNavigationHandler.navigateToUsernamePasswordLogin()
                },
                onBackPressed = { navigationHandler.navigateBack() },
                viewModel = sharedProfilesViewModel,
                otpViewModel = otpViewModel
            )
        }

        composable(Navigation.Auth.UsernamePasswordLogin.route) {
            var isLoading by remember { mutableStateOf(false) }

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


            if (isLoading) {
                LoadingScreen()
            } else {
                val securityCodeAvailable =
                    credentialsViewModel.credentialsState.value.securityCode != null

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
                                sharedProfilesViewModel.updateUserProfiles(listOf(it))
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

        composable(Navigation.Screen.Recovery.route) {

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


            RecoveryScreen(
                navigateToLogin = { authNavigationHandler.navigateToSignUpSignIn() },
                viewModel = sharedProfilesViewModel,
                onNavigateBack = { navigationHandler.navigateBack() }
            )
        }


        composable(Navigation.Auth.PinLogin.route) {

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


            PinLoginScreen(
                onLoginSuccess = { userProfile ->
                    sharedProfilesViewModel.updateUserProfiles(listOf(userProfile))
                    navigationHandler.navigateToUserProfile(userProfile.firstName)
                },
                onNavigate = { screen: Navigation.Screen -> navigationHandler.navigateTo(screen) },
                onPostNavigate = { navigationHandler.navigateToPosts() }
            )
        }


        composable(Navigation.Screen.Posts.route) {
            PostsScreen(onNavigate = {
                navigationHandler.navigateBack()
            })
        }


        composable(Navigation.Screen.UserProfileBob.route) { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username") ?: "Bob"

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val timerViewModel = viewModel<TimerViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

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

        composable(Navigation.Screen.UserProfileAlice.route) { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username") ?: "Alice"

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val timerViewModel = viewModel<TimerViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

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

        composable(Navigation.Screen.UserProfileEve.route) { navBackStackEntry ->
            val username = navBackStackEntry.arguments?.getString("username") ?: "Eve"

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val timerViewModel = viewModel<TimerViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

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

        composable(Navigation.Screen.InfoScreen.route) {
            InfoScreen(onNavigateBack = { navigationHandler.navigateBack() })

        }


        composable(Navigation.Screen.EditProfile.route) {

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


            EditProfile(
                userProfiles = sharedProfilesViewModel.userProfiles.value,
                onBackNavigate = {
                    navigationHandler.navigateBack()
                },
                isEditScreen = true,
                viewModel = sharedProfilesViewModel
            )
        }


        composable(Navigation.Screen.Settings.route) {

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


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

                        "permissions" -> navigationHandler.navigateToPermissionScreen()
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

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )


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


        composable(Navigation.Screen.PermissionScreen.route) {

            val permissionStateViewModel = viewModel<PermissionStateViewModel>()

            PermissionScreen(
                permissionStateViewModel = permissionStateViewModel,
                onBackButtonClick = { navigationHandler.navigateBack() }
            )
        }


    }
}