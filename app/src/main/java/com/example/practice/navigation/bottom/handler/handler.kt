package com.example.practice.navigation.bottom.handler



sealed class BottomNavigationHandler {
    data object Back : BottomNavigationHandler()
    data object EditProfile : BottomNavigationHandler()

    data object Messaging: BottomNavigationHandler()

    data object Settings: BottomNavigationHandler()
}

fun navigateTo(navEvent: BottomNavigationHandler, onBack: () -> Unit, onNavigate: (String) -> Unit) {
    when (navEvent) {

        is BottomNavigationHandler.Back -> onBack.invoke()

        is BottomNavigationHandler.EditProfile -> {
            onNavigate("edit")
        }
        is BottomNavigationHandler.Messaging -> {
            onNavigate("messaging")
        }
        is BottomNavigationHandler.Settings -> {
            onNavigate("settings")
        }

    }
}
