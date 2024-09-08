package com.example.practice.screens.securitycode


import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.profiles.viewmodel.credentials.CredentialsViewModel
import com.example.practice.room.UserCredentialsDao
import com.example.practice.screens.security.code.SecurityCodeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class SecurityCodeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock navigation and other callbacks
    private val mockOnNavigate = mock<(String) -> Unit>()
    private val mockUserCredentialsDao = mock<UserCredentialsDao>()
    private val viewModel = CredentialsViewModel(mockUserCredentialsDao)

    @Test
    fun testValidSecurityCodeInputAndNavigation() {
        // Set up initial state in ViewModel
        val initialSecurityCode = "1234"
        viewModel.setSecurityCode(initialSecurityCode)

        composeTestRule.setContent {
            SecurityCodeScreen(
                viewModel = viewModel,
                onNavigate = mockOnNavigate,
                securityCode = "" // Start with an empty security code
            )
        }

        // Simulate the user entering the correct security code
        composeTestRule.onNodeWithText("Enter Security Code").performTextInput(initialSecurityCode)
        composeTestRule.onNodeWithText("Continue").performClick()


        verify(mockOnNavigate).invoke("usernamePasswordLogin")
    }

    @Test
    fun testInvalidSecurityCodeShowsError() {
        // Set up initial state in ViewModel
        val initialSecurityCode = "1234"
        viewModel.setSecurityCode(initialSecurityCode)

        composeTestRule.setContent {
            SecurityCodeScreen(
                viewModel = viewModel,
                onNavigate = mockOnNavigate,
                securityCode = "" // Start with an empty security code
            )
        }

        // Simulate the user entering an incorrect security code
        composeTestRule.onNodeWithText("Enter Security Code").performTextInput("5678")
        composeTestRule.onNodeWithText("Continue").performClick()

        // Verify that an error state is triggered by checking the UI
        composeTestRule.onNodeWithText("Invalid security code. Please try again.").assertExists()
    }

    @Test
    fun testEmptySecurityCodeShowsError() {
        // Set up initial state in ViewModel
        val initialSecurityCode = ""
        viewModel.setSecurityCode(initialSecurityCode)

        composeTestRule.setContent {
            SecurityCodeScreen(
                viewModel = viewModel,
                onNavigate = mockOnNavigate,
                securityCode = initialSecurityCode // Start with an empty security code
            )
        }

        // Attempt to continue without entering a security code
        composeTestRule.onNodeWithText("Continue").performClick()

        // Verify that an error state is triggered by checking the UI
        composeTestRule.onNodeWithText("Invalid security code. Please try again.").assertExists()
    }
}