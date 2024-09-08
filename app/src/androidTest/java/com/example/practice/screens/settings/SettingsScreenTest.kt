package com.example.practice.screens.settings

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.ui.theme.PracticeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock navigation and other callbacks
    private val mockOnNavigate = mock<(String) -> Unit>()
    private val mockOnSaveCredentials = mock<(String, String) -> Unit>()
    private val mockOnShowConfirmationDialog = mock<() -> Unit>()
    private val mockOnSecurityCodeChange = mock<(String) -> Unit>()
    private val mockOnSecurityCodeSave = mock<() -> Unit>()
    private val mockSetDarkMode = mock<(Boolean) -> Unit>()
    private val mockSetSecuritySwitch = mock<(Boolean) -> Unit>()

    @Test
    fun testEmptyUsernameAndPassword() {
        composeTestRule.setContent {
            PracticeTheme {
                SettingsContent(
                    firstName = "Bob",
                    lastName = "Johnson",
                    darkMode = false,
                    securityEnabled = false,
                    signupEmail = "",
                    username = "",
                    password = "",
                    enteredSecurityCode = "",
                    onNavigate = mockOnNavigate,
                    onSaveCredentials = { username, password ->
                        // Verify that the callback is invoked with empty username and password
                        verify(mockOnSaveCredentials).invoke(username, password)
                    },
                    onSignupEmailChange = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onSecurityCodeChange = {},
                    onSecurityCodeSave = {},
                    onShowConfirmationDialog = mockOnShowConfirmationDialog,
                    onDismissConfirmationDialog = {},
                    setDarkMode = mockSetDarkMode,
                    setSecuritySwitch = mockSetSecuritySwitch
                )
            }
        }

        // Click the "Save Changes" button
        composeTestRule.onNodeWithText("Save Changes").performClick()

        // Verify that the confirmation dialog is shown
        verify(mockOnShowConfirmationDialog).invoke()
    }

    @Test
    fun testNavigateToSecurityCodeScreen() {
        composeTestRule.setContent {
            PracticeTheme {
                SettingsContent(
                    firstName = "Bob",
                    lastName = "Johnson",
                    darkMode = false,
                    securityEnabled = true,
                    signupEmail = "",
                    username = "user",
                    password = "pass",
                    enteredSecurityCode = "1234",
                    onNavigate = mockOnNavigate,
                    onSaveCredentials = { username, password ->
                    },
                    onSignupEmailChange = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onSecurityCodeChange = mockOnSecurityCodeChange,
                    onSecurityCodeSave = mockOnSecurityCodeSave,
                    onShowConfirmationDialog = {},
                    onDismissConfirmationDialog = {},
                    setDarkMode = {},
                    setSecuritySwitch = {}
                )
            }
        }

        // Click the "Save Security Code" button
        composeTestRule.onNodeWithText("Save Security Code").performClick()

        // Verify that the onSecurityCodeSave callback is invoked
        verify(mockOnSecurityCodeSave).invoke()

        // Verify that the onNavigate callback is invoked with "securityCode"
        verify(mockOnNavigate).invoke("securityCode")

        // Verify that onSaveCredentials was not called
        verify(mockOnSaveCredentials, never()).invoke(any(), any())
    }

    @Test
    fun testNavigationWithEmptyUsernameAndPassword() {
        composeTestRule.setContent {
            PracticeTheme {
                SettingsContent(
                    firstName = "Bob",
                    lastName = "Johnson",
                    darkMode = false,
                    securityEnabled = false,
                    signupEmail = "",
                    username = "",
                    password = "",
                    enteredSecurityCode = "",
                    onNavigate = mockOnNavigate,
                    onSaveCredentials = { username, password ->
                        // Simulate user confirmation
                        mockOnSaveCredentials.invoke(username, password)
                        // Trigger confirmation dialog display
                        mockOnShowConfirmationDialog.invoke()
                    },
                    onSignupEmailChange = {},
                    onUsernameChange = {},
                    onPasswordChange = {},
                    onSecurityCodeChange = {},
                    onSecurityCodeSave = {},
                    onShowConfirmationDialog = mockOnShowConfirmationDialog,
                    onDismissConfirmationDialog = {},
                    setDarkMode = {},
                    setSecuritySwitch = {}
                )
            }
        }

        // Click the "Save Changes" button to trigger the dialog
        composeTestRule.onNodeWithText("Save Changes").performClick()

        // Click the "Confirm" button in the dialog
        composeTestRule.onNodeWithText("Confirm").performClick()

        // Verify that the correct navigation action was taken
        verify(mockOnNavigate).invoke("usernamePasswordLogin")
    }
}
