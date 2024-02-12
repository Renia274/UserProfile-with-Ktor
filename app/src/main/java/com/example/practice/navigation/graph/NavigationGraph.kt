package com.example.practice.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice.screens.userprofile.editprofile.EditProfile
import com.example.practice.R
import com.example.practice.UserProfilesLoading
import com.example.practice.data.UserData
import com.example.practice.data.message.Message
import com.example.practice.ktor.screens.posts.PostsScreen
import com.example.practice.logs.navigation.CrashlyticsNavigationLogger
import com.example.practice.logs.navigation.NavigationAnalyticsLogger
import com.example.practice.navigation.handlers.AuthNavigationHandler
import com.example.practice.navigation.handlers.NavigationHandler
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewModel
import com.example.practice.profiles.viewmodel.timer.TimerViewModel
import com.example.practice.screens.info.InfoScreen
import com.example.practice.screen.components.Loader
import com.example.practice.screens.otp.OtpScreen
import com.example.practice.screens.permission.PermissionScreen
import com.example.practice.screens.pin.PinLoginScreen
import com.example.practice.screens.recovery.RecoveryScreen
import com.example.practice.screens.security.code.SecurityCodeScreen
import com.example.practice.screens.settings.SettingsScreen
import com.example.practice.screens.sinup.signin.SignUpSignInScreen
import com.example.practice.screens.signup.SignupScreen
import com.example.practice.screens.splash.screen.SplashScreen
import com.example.practice.screens.login.UsernamePasswordLoginScreen
import com.example.practice.screens.messaging.MessagingScreen

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
            val MessagingScreen = Screen("messaging")
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

    NavigationAnalyticsLogger.initialize(LocalContext.current)
    CrashlyticsNavigationLogger.initialize()

    NavHost(
        navController = navController,
        startDestination = Navigation.Screen.Splash.route
    ) {
        composable(Navigation.Screen.Splash.route) {

            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.SignUpSignIn.route)

            SplashScreen {
                authNavigationHandler.navigateToSignUpSignIn()
            }
        }


        composable(Navigation.Auth.SignUpSignIn.route) {

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Auth.SignUpSignIn.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.SignUpSignIn.route)

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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Auth.Signup.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.Signup.route)

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


            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Auth.OtpScreen.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.OtpScreen.route)

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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Auth.UsernamePasswordLogin.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.UsernamePasswordLogin.route)

            if (isLoading) {
                Loader()
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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.Recovery.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.Recovery.route)

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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Auth.PinLogin.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.PinLogin.route)

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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.Posts.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.Posts.route)

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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.UserProfileBob.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.UserProfileBob.route)

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

                        "messaging" -> {
                            navigationHandler.navigateToMessagingScreen()
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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.UserProfileAlice.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.UserProfileAlice.route)

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

                        "messaging" -> {
                            navigationHandler.navigateToMessagingScreen()
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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.UserProfileEve.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.UserProfileEve.route)

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

                        "messaging" -> {
                            navigationHandler.navigateToMessagingScreen()
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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.InfoScreen.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.InfoScreen.route)

            InfoScreen(onNavigateBack = { navigationHandler.navigateBack() })

        }


        composable(Navigation.Screen.EditProfile.route) {

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.EditProfile.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.EditProfile.route)

            EditProfile(
                userProfiles = sharedProfilesViewModel.userProfiles.value,
                onBackNavigate = {
                    navigationHandler.navigateBack()
                },
                isEditScreen = true,
                viewModel = sharedProfilesViewModel
            )
        }


        composable(Navigation.Screen.MessagingScreen.route) {

            val messages = remember { mutableStateListOf<Message>() }
            val onSendMessage: (String, String) -> Unit = { newMessage, recipient ->
                messages.add(Message(newMessage, "Me"))
            }

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.MessagingScreen.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.MessagingScreen.route)

            MessagingScreen(
                messages = messages,
                onSendMessage = onSendMessage,
                sender="Me",
                onBackClicked = { navigationHandler.navigateBack()}
            )
        }


        composable(Navigation.Screen.Settings.route) {

            val credentialsViewModel = viewModel<CredentialsViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            val sharedProfilesViewModel = viewModel<SharedProfilesViewModel>(
                viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
            )

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.Settings.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.Settings.route)


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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Auth.SecurityCode.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Auth.SecurityCode.route)

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

            NavigationAnalyticsLogger.logNavigationEvent(Navigation.Screen.PermissionScreen.route)
            CrashlyticsNavigationLogger.logNavigationEvent(Navigation.Screen.PermissionScreen.route)

            PermissionScreen(
                onBackButtonClick = { navigationHandler.navigateBack() }
            )
        }


    }
}