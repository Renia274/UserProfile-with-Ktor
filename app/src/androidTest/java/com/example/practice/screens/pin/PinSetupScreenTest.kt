package com.example.practice.screens.pin

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.profiles.viewmodel.pin.PinViewModel
import com.example.practice.ui.theme.PracticeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class PinSetupScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockPinViewModel = mock<PinViewModel>()
    private val testPin = "123456"

    @Test
    fun testInitialUIRendering() {
        composeTestRule.setContent {
            PracticeTheme {
                PinSetupScreen(
                    onNavigate = {},
                    onBack = {},
                    pinViewModel = mockPinViewModel
                )
            }
        }

        // Verify that the UI components are displayed
        composeTestRule.onNodeWithText("Enter PIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm PIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Save PIN").assertIsDisplayed()
    }

    @Test
    fun testPinVisibilityToggle() {
        composeTestRule.setContent {
            PracticeTheme {
                PinSetupScreen(
                    onNavigate = {},
                    onBack = {},
                    pinViewModel = mockPinViewModel
                )
            }
        }

        // Verify that PIN is hidden by default
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(testPin)
        composeTestRule.onNodeWithText(testPin).assertDoesNotExist()

        // Click the toggle visibility icon
        composeTestRule.onNodeWithContentDescription("Show pin").performClick()

        // Verify that PIN is now visible
        composeTestRule.onNodeWithText(testPin).assertIsDisplayed()
    }

    @Test
    fun testErrorMessageDisplayWhenPinsDoNotMatch() {
        composeTestRule.setContent {
            PracticeTheme {
                PinSetupScreen(
                    onNavigate = {},
                    onBack = {},
                    pinViewModel = mockPinViewModel
                )
            }
        }

        // Input PIN and a non-matching confirmation PIN
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(testPin)
        composeTestRule.onNodeWithText("Confirm PIN").performTextInput("654321")
        composeTestRule.onNodeWithText("Save PIN").performClick()

        // Verify that the error message is displayed
        composeTestRule.onNodeWithText("Pins do not match or are not 6 digits long").assertIsDisplayed()
    }

    @Test
    fun testErrorMessageDisplayWhenPinsAreNotSixDigits() {
        composeTestRule.setContent {
            PracticeTheme {
                PinSetupScreen(
                    onNavigate = {},
                    onBack = {},
                    pinViewModel = mockPinViewModel
                )
            }
        }

        // Input PIN and a matching confirmation PIN but not 6 digits
        composeTestRule.onNodeWithText("Enter PIN").performTextInput("123")
        composeTestRule.onNodeWithText("Confirm PIN").performTextInput("123")
        composeTestRule.onNodeWithText("Save PIN").performClick()

        // Verify that the error message is displayed
        composeTestRule.onNodeWithText("Pins do not match or are not 6 digits long").assertIsDisplayed()
    }

    @Test
    fun testSuccessfulPinSave() {
        val mockOnNavigate = mock<() -> Unit>()

        composeTestRule.setContent {
            PracticeTheme {
                PinSetupScreen(
                    onNavigate = mockOnNavigate,
                    onBack = {},
                    pinViewModel = mockPinViewModel
                )
            }
        }

        // Input matching PIN and confirmation PIN
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(testPin)
        composeTestRule.onNodeWithText("Confirm PIN").performTextInput(testPin)
        composeTestRule.onNodeWithText("Save PIN").performClick()

        // Verify that the savePinForProfile was called for each user
        verify(mockPinViewModel).savePinForProfile("Bob", testPin)
        verify(mockPinViewModel).savePinForProfile("Alice", testPin)
        verify(mockPinViewModel).savePinForProfile("Eve", testPin)

        // Verify that the navigation callback was invoked
        verify(mockOnNavigate).invoke()
    }

    @Test
    fun testBackNavigation() {
        val mockOnBack = mock<() -> Unit>()

        composeTestRule.setContent {
            PracticeTheme {
                PinSetupScreen(
                    onNavigate = {},
                    onBack = mockOnBack,
                    pinViewModel = mockPinViewModel
                )
            }
        }

        // Click the back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify that the back navigation callback was invoked
        verify(mockOnBack).invoke()
    }
}

