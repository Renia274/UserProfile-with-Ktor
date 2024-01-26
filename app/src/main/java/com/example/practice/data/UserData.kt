package com.example.practice.data

data class UserData(
    val firstName: String,
    val lastName: String,
    val imageResId: Int,
    var profession: String = "",
    var savedProfession: String = "",
    var interests: List<String> = emptyList()

)




data class UserCredentials(val username: String, val password: String)





