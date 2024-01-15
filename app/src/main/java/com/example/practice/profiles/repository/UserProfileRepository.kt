package com.example.practice.profiles.repository

import androidx.lifecycle.MutableLiveData
import com.example.practice.R
import com.example.practice.data.UserData
import javax.inject.Inject

class UserRepository @Inject constructor(

) {

    private val profiles = listOf(
        UserData("Bob", "Johnson", R.drawable.bob_johnson),
        UserData("Alice", "Smith", R.drawable.alice_smith),
        UserData("Eve", "Brown", R.drawable.eve_brown)
    )

    private val userProfiles = MutableLiveData<List<UserData>>()

    init {
        // Initialize the profiles
        userProfiles.value = profiles
    }

    fun saveProfession(imageResId: Int, profession: String) {
        val userProfilesList = userProfiles.value.orEmpty().toMutableList()
        val userProfile = userProfilesList.find { it.imageResId == imageResId }
        userProfile?.let {
            it.savedProfession = profession
        }
        userProfiles.value = userProfilesList
    }

    fun saveInterests(imageResId: Int, interests: List<String>) {
        val userProfilesList = userProfiles.value.orEmpty().toMutableList()
        val userProfile = userProfilesList.find { it.imageResId == imageResId }
        userProfile?.let {
            it.interests = interests
        }
        userProfiles.value = userProfilesList
    }



}
