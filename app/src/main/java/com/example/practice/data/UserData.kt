package com.example.practice.data

import androidx.compose.ui.graphics.Color

data class UserData(
    val firstName: String,
    val lastName: String,
    val imageResId: Int,
    var profession: String = "",
    var savedProfession: String = "",

)

data class BottomNavItem(
    val label: String,
    val iconResId: Int,
    val destination: String,
    val itemColor: Color
)


data class UserCredentials(val username: String, val password: String)