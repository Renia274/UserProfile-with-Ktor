package com.example.practice.profiles.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.practice.R
import com.example.practice.profiles.utils.getOrAwaitValue
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        userRepository = UserRepository()
    }


    @Test
    fun testInitialProfiles() {
        val profiles = userRepository.getUserProfiles().getOrAwaitValue()

        assertEquals(3, profiles.size)
        assertEquals("Bob", profiles[0].firstName)
        assertEquals("Alice", profiles[1].firstName)
        assertEquals("Eve", profiles[2].firstName)
    }


    @Test
    fun testSaveProfession() {
        val imageResId = R.drawable.bob_johnson
        val profession = "Engineer"

        userRepository.saveProfession(imageResId, profession)

        val updatedProfile = userRepository.getUserProfiles().getOrAwaitValue().find { it.imageResId == imageResId }
        assertEquals(profession, updatedProfile?.savedProfession)
    }

    @Test
    fun testSaveInterests() {
        val imageResId = R.drawable.bob_johnson
        val interests = listOf("Reading", "Coding")

        userRepository.saveInterests(imageResId, interests)

        val updatedProfile = userRepository.getUserProfiles().getOrAwaitValue().find { it.imageResId == imageResId }
        assertEquals(interests, updatedProfile?.interests)
    }
}
