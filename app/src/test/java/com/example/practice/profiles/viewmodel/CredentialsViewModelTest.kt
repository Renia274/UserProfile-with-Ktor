package com.example.practice.profiles.viewmodel

import org.mockito.Mockito.verify




import com.example.practice.data.UserCredentials
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.room.UserCredentialsDao
import com.example.practice.room.UserCredentialsEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class CredentialsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockUserCredentialsDao = mock<UserCredentialsDao>()
    private val viewModel = CredentialsViewModel(mockUserCredentialsDao)

    @Test
    fun testSetEnteredCredentials() {
        val username = "testUser"
        val password = "testPass"

        viewModel.setEnteredCredentials(username, password)

        assertEquals(UserCredentials(username, password), viewModel.credentialsState.value.enteredCredentials)
    }

    @Test
    fun testSaveUserCredentials(): Unit = runBlocking {
        val username = "testUser"
        val password = "testPass"
        val userCredentialsEntity = UserCredentialsEntity(username, password)

        viewModel.saveUserCredentials(username, password)

        verify(mockUserCredentialsDao).insertUserCredentials(userCredentialsEntity)
    }
}
