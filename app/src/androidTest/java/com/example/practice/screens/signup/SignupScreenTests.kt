package com.example.practice.screens.signup

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.MainActivity
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.ui.theme.PracticeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class SignupScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val mockCredentialsViewModel = mock<CredentialsViewModel>()
    private val mockSharedViewModel = mock<SharedProfilesViewModel>()

    private fun setupContent(
        onNavigateToLogin: () -> Unit = {},
        onNavigate: () -> Unit = {}
    ) {
        composeTestRule.setContent {
            PracticeTheme {
                SignupScreen(
                    onNavigateToLogin = onNavigateToLogin,
                    onNavigate = onNavigate,
                    credentialsViewModel = mockCredentialsViewModel,
                    sharedViewModel = mockSharedViewModel
                )
            }
        }
    }

    @Test
    suspend fun testSuccessfulSignup() {
        // Create a variable to track if navigation happened
        val navigateCalled = mutableListOf<Boolean>()
        setupContent(onNavigate = { navigateCalled.add(true) })

        // Enter valid username
        composeTestRule.onNodeWithText("Username").performTextInput("validuser")

        // Enter valid email
        composeTestRule.onNodeWithText("Email").performTextInput("user@example.com")

        // Enter valid password
        composeTestRule.onNodeWithText("Password").performTextInput("Password1")

        // Click the Sign Up button
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // Verify interactions with ViewModels
        verify(mockCredentialsViewModel).setEnteredCredentials("validuser", "Password1")
        verify(mockCredentialsViewModel).saveUserCredentials("validuser", "Password1")
        verify(mockSharedViewModel).setSignupEmail("user@example.com")

        // Assert that onNavigate was called
        assert(navigateCalled.contains(true))
    }

    @Test
    suspend fun testInvalidEmail() {
        setupContent()

        // Enter valid username
        composeTestRule.onNodeWithText("Username").performTextInput("validuser")

        // Enter invalid email
        composeTestRule.onNodeWithText("Email").performTextInput("user@.com")

        // Enter valid password
        composeTestRule.onNodeWithText("Password").performTextInput("Password1")

        // Click the Sign Up button
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // Assert that the error message is shown
        composeTestRule.onNodeWithText("Invalid email or password format").assertIsDisplayed()

        // Verify no interactions with ViewModels
        verify(mockCredentialsViewModel, never()).setEnteredCredentials(any(), any())
        verify(mockCredentialsViewModel, never()).saveUserCredentials(any(), any())
        verify(mockSharedViewModel, never()).setSignupEmail(any())
    }

    @Test
    suspend fun testShortPassword() {
        setupContent()

        // Enter valid username
        composeTestRule.onNodeWithText("Username").performTextInput("validuser")

        // Enter valid email
        composeTestRule.onNodeWithText("Email").performTextInput("user@example.com")

        // Enter short password
        composeTestRule.onNodeWithText("Password").performTextInput("short1")

        // Click the Sign Up button
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // Assert that the error message is shown
        composeTestRule.onNodeWithText("Invalid email or password format").assertIsDisplayed()

        // Verify no interactions with ViewModels
        verify(mockCredentialsViewModel, never()).setEnteredCredentials(any(), any())
        verify(mockCredentialsViewModel, never()).saveUserCredentials(any(), any())
        verify(mockSharedViewModel, never()).setSignupEmail(any())
    }

    @Test
    suspend fun testPasswordWithoutUppercase() {
        setupContent()

        // Enter valid username
        composeTestRule.onNodeWithText("Username").performTextInput("validuser")

        // Enter valid email
        composeTestRule.onNodeWithText("Email").performTextInput("user@example.com")

        // Enter password without uppercase
        composeTestRule.onNodeWithText("Password").performTextInput("password1")

        // Click the Sign Up button
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // Assert that the error message is shown
        composeTestRule.onNodeWithText("Invalid email or password format").assertIsDisplayed()

        // Verify no interactions with ViewModels
        verify(mockCredentialsViewModel, never()).setEnteredCredentials(any(), any())
        verify(mockCredentialsViewModel, never()).saveUserCredentials(any(), any())
        verify(mockSharedViewModel, never()).setSignupEmail(any())
    }

    @Test
    suspend fun testPasswordWithoutNumber() {
        setupContent()

        // Enter valid username
        composeTestRule.onNodeWithText("Username").performTextInput("validuser")

        // Enter valid email
        composeTestRule.onNodeWithText("Email").performTextInput("user@example.com")

        // Enter password without numbers
        composeTestRule.onNodeWithText("Password").performTextInput("Password!")

        // Click the Sign Up button
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // Assert that the error message is shown
        composeTestRule.onNodeWithText("Invalid email or password format").assertIsDisplayed()

        // Verify no interactions with ViewModels
        verify(mockCredentialsViewModel, never()).setEnteredCredentials(any(), any())
        verify(mockCredentialsViewModel, never()).saveUserCredentials(any(), any())
        verify(mockSharedViewModel, never()).setSignupEmail(any())
    }

    @Test
    suspend fun testEmptyUsername() {
        setupContent()

        // Enter empty username
        composeTestRule.onNodeWithText("Username").performTextInput("")

        // Enter valid email
        composeTestRule.onNodeWithText("Email").performTextInput("user@example.com")

        // Enter valid password
        composeTestRule.onNodeWithText("Password").performTextInput("Password1")

        // Click the Sign Up button
        composeTestRule.onNodeWithText("Sign Up").performClick()

        // Assert that the error message is shown
        composeTestRule.onNodeWithText("Invalid email or password format").assertIsDisplayed()

        // Verify no interactions with ViewModels
        verify(mockCredentialsViewModel, never()).setEnteredCredentials(any(), any())
        verify(mockCredentialsViewModel, never()).saveUserCredentials(any(), any())
        verify(mockSharedViewModel, never()).setSignupEmail(any())
    }
}