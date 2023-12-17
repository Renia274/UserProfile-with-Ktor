package com.example.practice.profiles.repository

import androidx.lifecycle.MutableLiveData
import com.example.practice.R
import com.example.practice.data.UserData
import javax.inject.Inject

class UserRepository @Inject constructor() {

    private val profiles = listOf(
        UserData("Bob", "Johnson", R.drawable.bob_johnson),
        UserData("Alice", "Smith", R.drawable.alice_smith),
        UserData("Eve", "Brown", R.drawable.eve_brown)
    )


    private val _userProfiles = MutableLiveData<List<UserData>>()

    init {
        // Initialize the profiles
        _userProfiles.value = profiles
    }



    fun saveProfession(imageResId: Int, profession: String) {
        val userProfiles = _userProfiles.value.orEmpty().toMutableList()
        val userProfile = userProfiles.find { it.imageResId == imageResId }
        userProfile?.let {
            it.savedProfession = profession
        }
        _userProfiles.value = userProfiles
    }



}
