package com.example.practice.logs.navigation

import com.example.practice.navigation.graph.Navigation
import android.os.Bundle
import com.example.practice.services.CrashlyticsService
import com.example.practice.services.FirebaseAnalyticsService


object NavigationLogger {

    private val firebaseAnalyticsService: FirebaseAnalyticsService = FirebaseAnalyticsService
    private val crashlyticsService: CrashlyticsService = CrashlyticsService

    private const val LABEL_SPLASH = "Splash Screen"
    private const val LABEL_SIGN_UP_SIGN_IN = "Sign Up / Sign In Screen"
    private const val LABEL_SIGNUP = "Signup Screen"
    private const val LABEL_OTP_SCREEN = "OTP Screen"
    private const val LABEL_USERNAME_PASSWORD_LOGIN = "Login Screen"
    private const val LABEL_RECOVERY = "Recovery Screen"
    private const val LABEL_PIN_LOGIN = "PIN Login Screen"
    private const val LABEL_POSTS = "Posts Screen"
    private const val LABEL_USER_PROFILE_BOB = "User Profile Bob Screen"
    private const val LABEL_USER_PROFILE_ALICE = "User Profile Alice Screen"
    private const val LABEL_USER_PROFILE_EVE = "User Profile Eve Screen"
    private const val LABEL_INFO_SCREEN = "Info Screen"
    private const val LABEL_EDIT_PROFILE = "Edit Profile Screen"
    private const val LABEL_MESSAGING_SCREEN = "Messaging Screen"
    private const val LABEL_SETTINGS = "Settings Screen"
    private const val LABEL_SECURITY_CODE = "Security Code Screen"
    private const val LABEL_PERMISSION_SCREEN = "Permission Screen"


    private fun logNavigationEvent(route: String, label: String) {
        val bundle = Bundle().apply {
            putString("route", route)
            putString("label", label)
        }
        firebaseAnalyticsService.logAnalyticsEvent("navigation_event", bundle)
        crashlyticsService.logCrashlyticsEvent("Navigation: $label")
    }

    fun logAllNavigationEvents() {
        logNavigationEvent(Navigation.Screen.Splash.route, LABEL_SPLASH)
        logNavigationEvent(Navigation.Auth.SignUpSignIn.route, LABEL_SIGN_UP_SIGN_IN)
        logNavigationEvent(Navigation.Auth.Signup.route, LABEL_SIGNUP)
        logNavigationEvent(Navigation.Auth.OtpScreen.route, LABEL_OTP_SCREEN)
        logNavigationEvent(Navigation.Auth.UsernamePasswordLogin.route, LABEL_USERNAME_PASSWORD_LOGIN)
        logNavigationEvent(Navigation.Screen.Recovery.route, LABEL_RECOVERY)
        logNavigationEvent(Navigation.Auth.PinLogin.route, LABEL_PIN_LOGIN)
        logNavigationEvent(Navigation.Screen.Posts.route, LABEL_POSTS)
        logNavigationEvent(Navigation.Screen.UserProfileBob.route, LABEL_USER_PROFILE_BOB)
        logNavigationEvent(Navigation.Screen.UserProfileAlice.route, LABEL_USER_PROFILE_ALICE)
        logNavigationEvent(Navigation.Screen.UserProfileEve.route, LABEL_USER_PROFILE_EVE)
        logNavigationEvent(Navigation.Screen.InfoScreen.route, LABEL_INFO_SCREEN)
        logNavigationEvent(Navigation.Screen.EditProfile.route, LABEL_EDIT_PROFILE)
        logNavigationEvent(Navigation.Screen.MessagingScreen.route, LABEL_MESSAGING_SCREEN)
        logNavigationEvent(Navigation.Screen.Settings.route, LABEL_SETTINGS)
        logNavigationEvent(Navigation.Auth.SecurityCode.route, LABEL_SECURITY_CODE)
        logNavigationEvent(Navigation.Screen.PermissionScreen.route, LABEL_PERMISSION_SCREEN)
    }


}
