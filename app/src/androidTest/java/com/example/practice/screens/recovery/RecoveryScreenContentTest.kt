package com.example.practice.screens.recovery

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.ui.theme.PracticeTheme
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


@RunWith(AndroidJUnit4::class)
class RecoveryScreenContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel = mock(SharedProfilesViewModel::class.java)
    private val mockNavigateToLogin = mock<() -> Unit>()
    private val mockOnNavigateBack = mock<() -> Unit>()
    private val signupEmail = "test@example.com"

    @Test
    fun testEmptyEmailField() {
        // Set up the view model to return the signup email
        `when`(mockViewModel.stateFlow.value.signupEmail).thenReturn(signupEmail)

        composeTestRule.setContent {
            PracticeTheme {
                RecoveryScreenContent(
                    navigateToLogin = mockNavigateToLogin,
                    onNavigateBack = mockOnNavigateBack,
                    signupEmail = signupEmail
                )
            }
        }

        // Click the reset button with an empty email field
        composeTestRule.onNodeWithText("Reset").performClick()

        // Check if the error message is displayed
        composeTestRule.onNodeWithText("Entered email doesn't match the signup email.").assertIsDisplayed()

        // Verify that the navigateToLogin callback was not called
        verify(mockNavigateToLogin, never()).invoke()
    }

    @Test
    fun testIncorrectEmailField() {
        // Set up the view model to return the signup email
        `when`(mockViewModel.stateFlow.value.signupEmail).thenReturn(signupEmail)

        composeTestRule.setContent {
            PracticeTheme {
                RecoveryScreenContent(
                    navigateToLogin = mockNavigateToLogin,
                    onNavigateBack = mockOnNavigateBack,
                    signupEmail = signupEmail
                )
            }
        }

        // Input an incorrect email and click the reset button
        composeTestRule.onNodeWithText("Enter your email").performTextInput("wrong@example.com")
        composeTestRule.onNodeWithText("Reset").performClick()

        // Check if the error message is displayed
        composeTestRule.onNodeWithText("Entered email doesn't match the signup email.").assertIsDisplayed()

        // Verify that the navigateToLogin callback was not called
        verify(mockNavigateToLogin, never()).invoke()
    }

    @Test
    fun testCorrectEmailField() {
        // Set up the view model to return the signup email
        `when`(mockViewModel.stateFlow.value.signupEmail).thenReturn(signupEmail)

        composeTestRule.setContent {
            PracticeTheme {
                RecoveryScreenContent(
                    navigateToLogin = mockNavigateToLogin,
                    onNavigateBack = mockOnNavigateBack,
                    signupEmail = signupEmail
                )
            }
        }

        // Input the correct email and click the reset button
        composeTestRule.onNodeWithText("Enter your email").performTextInput(signupEmail)
        composeTestRule.onNodeWithText("Reset").performClick()

        // Check if the recovery email sent message is displayed
        composeTestRule.onNodeWithText("Recovery email sent to $signupEmail. Please check your email.").assertIsDisplayed()

        // Verify that the navigateToLogin callback was invoked
        verify(mockNavigateToLogin, times(1)).invoke()
    }

    @Test
    fun testNavigateBack() {
        // Set up the view model to return the signup email
        `when`(mockViewModel.stateFlow.value.signupEmail).thenReturn(signupEmail)

        composeTestRule.setContent {
            PracticeTheme {
                RecoveryScreenContent(
                    navigateToLogin = mockNavigateToLogin,
                    onNavigateBack = mockOnNavigateBack,
                    signupEmail = signupEmail
                )
            }
        }

        // Click the back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify that the onNavigateBack callback was invoked
        verify(mockOnNavigateBack, times(1)).invoke()
    }
}
