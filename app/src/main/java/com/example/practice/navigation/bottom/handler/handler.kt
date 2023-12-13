package com.example.practice.navigation.bottom.handler

sealed class BottomNavigation {
    data object Back : BottomNavigation()
    data object ShowImages : BottomNavigation()
}

fun navigateTo(navEvent: BottomNavigation, onBack: () -> Unit, onNavigate: (String) -> Unit) {
    when (navEvent) {
        is BottomNavigation.Back -> onBack.invoke()
        is BottomNavigation.ShowImages -> {
            onNavigate("images")
        }
        else -> {}
    }
}
