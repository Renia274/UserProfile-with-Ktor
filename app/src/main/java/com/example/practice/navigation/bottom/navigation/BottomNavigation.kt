package com.example.practice.navigation.bottom.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.example.practice.R
import com.example.practice.data.BottomNavItem
import com.example.practice.navigation.bottom.handler.BottomNavigation
import com.example.practice.screens.items.CustomBottomBar
import kotlinx.coroutines.flow.Flow


@Composable
fun BottomNavigationItems(
    selectedIndex: Flow<Int>,
    onNavigate: (BottomNavigation) -> Unit
) {

    val selectedIndexChanged by selectedIndex.collectAsState(initial = 0)


    CustomBottomBar(
        bottomNavigationItems = listOf(
            BottomNavItem("", R.drawable.ic__left, "back"),
            BottomNavItem("", R.drawable.ic___right, "images")
        ),
        selectedIndex = selectedIndexChanged,
        onItemSelected = { index ->
            when (index) {
                0 -> onNavigate(BottomNavigation.Back)
                1 -> onNavigate(BottomNavigation.ShowImages)
            }
        },
        backgroundColor = Color.Gray
    )
}
