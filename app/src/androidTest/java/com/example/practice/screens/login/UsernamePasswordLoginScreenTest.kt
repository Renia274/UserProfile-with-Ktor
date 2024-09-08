package com.example.practice.screens.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.room.UserCredentialsEntity
import com.example.practice.ui.theme.PracticeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class UsernamePasswordLoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockCredentialsViewModel = mock(CredentialsViewModel::class.java)
    private val mockOnLoginSuccess: (String, String, String, String) -> Unit = mock()
    private val mockOnLoading: (Boolean) -> Unit = mock()
    private val mockOnNavigateToRecovery: () -> Unit = mock()
    private val mockOnBack: () -> Unit = mock()

    private fun setContent() {
        composeTestRule.setContent {
            PracticeTheme {
                UsernamePasswordLoginScreen(
                    onLoginSuccess = mockOnLoginSuccess,
                    onLoading = mockOnLoading,
                    onNavigateToRecovery = mockOnNavigateToRecovery,
                    onBack = mockOnBack,
                    viewModel = mockCredentialsViewModel
                )
            }
        }
    }

    @Test
    suspend fun testSuccessfulLogin() {
        // Mocking ViewModel responses
        `when`(mockCredentialsViewModel.isValidCredentials("bob1", "1234567A")).thenReturn(true)

        // Mock loadCredentialsForLogin to return a mocked UserCredentials object
        val mockCredentials = mock(UserCredentialsEntity::class.java)
        `when`(mockCredentials.username).thenReturn("bob1")
        `when`(mockCredentials.password).thenReturn("1234567A")
        `when`(mockCredentialsViewModel.loadCredentialsForLogin("bob1", "1234567A"))
            .thenReturn(mockCredentials)

        setContent()

        // Enter valid username
        composeTestRule.onNodeWithText("Username").performTextInput("bob1")

        // Enter valid password
        composeTestRule.onNodeWithText("Password").performTextInput("1234567A")

        // Click the Login button
        composeTestRule.onNodeWithText("Login").performClick()

        // Verify that onLoginSuccess was called with correct parameters
        verify(mockOnLoginSuccess).invoke("bob1", "1234567A", "bob1", "1234567A")

        // Verify that ViewModel methods were called
        verify(mockCredentialsViewModel).isValidCredentials("bob1", "1234567A")
        verify(mockCredentialsViewModel).loadCredentialsForLogin("bob1", "1234567A")
    }

    @Test
    fun testInvalidLogin() {
        // Mocking ViewModel responses
        `when`(mockCredentialsViewModel.isValidCredentials("invaliduser", "wrongpass")).thenReturn(false)

        setContent()

        // Enter invalid username
        composeTestRule.onNodeWithText("Username").performTextInput("invaliduser")

        // Enter invalid password
        composeTestRule.onNodeWithText("Password").performTextInput("wrongpass")

        // Click the Login button
        composeTestRule.onNodeWithText("Login").performClick()

        // Verify that onLoginSuccess was not called
        verify(mockOnLoginSuccess, never()).invoke(any(), any(), any(), any())

        // Verify that ViewModel methods were called
        verify(mockCredentialsViewModel).isValidCredentials("invaliduser", "wrongpass")
    }

    @Test
    fun testPasswordVisibilityToggle() {
        setContent()

        // Initially, the password should be hidden
        composeTestRule.onNodeWithText("Password").assertDoesNotExist()

        // Toggle password visibility
        composeTestRule.onNodeWithContentDescription("Show password").performClick()

        // Check if password is visible now
        composeTestRule.onNodeWithText("1234567A").assertIsDisplayed()
    }

    @Test
    fun testForgotPasswordNavigation() {
        setContent()

        // Click the Forgot Password button
        composeTestRule.onNodeWithText("Forgot Password?").performClick()

        // Verify that onNavigateToRecovery was called
        verify(mockOnNavigateToRecovery).invoke()
    }

    @Test
    fun testBackNavigation() {
        setContent()

        // Click the Back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify that onBack was called
        verify(mockOnBack).invoke()
    }
}
