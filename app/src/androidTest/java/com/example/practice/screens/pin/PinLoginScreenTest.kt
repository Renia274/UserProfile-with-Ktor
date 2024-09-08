package com.example.practice.screens.pin

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.data.UserData
import com.example.practice.navigation.graph.Navigation
import com.example.practice.profiles.viewmodel.pin.PinViewModel
import com.example.practice.ui.theme.PracticeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import com.example.practice.R
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never

@RunWith(AndroidJUnit4::class)
class PinLoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockPinViewModel = mock(PinViewModel::class.java)
    private val mockOnLoginSuccess = mock<(UserData) -> Unit>()
    private val mockOnNavigate = mock<(Navigation.Screen) -> Unit>()
    private val mockOnPostNavigate = mock<() -> Unit>()
    private val bobPin = "123456"
    private val alicePin = "987654"
    private val evePin = "555555"

    @Test
    fun testInitialUIRendering() {
        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Verify that the UI components are displayed
        composeTestRule.onNodeWithText("Enter Your PIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter PIN").assertIsDisplayed()
        composeTestRule.onNodeWithText("Log In").assertIsDisplayed()
    }

    @Test
    fun testPinVisibilityToggle() {
        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Enter PIN and check that it is hidden by default
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(bobPin)
        composeTestRule.onNodeWithText(bobPin).assertDoesNotExist()

        // Click the toggle visibility icon
        composeTestRule.onNodeWithContentDescription("Show pin").performClick()

        // Verify that PIN is now visible
        composeTestRule.onNodeWithText(bobPin).assertIsDisplayed()
    }

    @Test
    fun testSuccessfulLoginBob() {
        // Use doReturn instead of whenever
        doReturn(bobPin).`when`(mockPinViewModel).getPinForProfile("Bob")

        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Enter the correct PIN for Bob and click Login
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(bobPin)
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify that the onLoginSuccess callback was invoked with the correct UserData
        verify(mockOnLoginSuccess).invoke(UserData("Bob", "Johnson", R.drawable.bob_johnson))
    }

    @Test
    fun testSuccessfulLoginAlice() {
        // Use doReturn instead of whenever
        doReturn(alicePin).`when`(mockPinViewModel).getPinForProfile("Alice")

        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Enter the correct PIN for Alice and click Login
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(alicePin)
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify that the onLoginSuccess callback was invoked with the correct UserData
        verify(mockOnLoginSuccess).invoke(UserData("Alice", "Smith", R.drawable.alice_smith))
    }

    @Test
    fun testSuccessfulLoginEve() {
        // Use doReturn instead of whenever
        doReturn(evePin).`when`(mockPinViewModel).getPinForProfile("Eve")

        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Enter the correct PIN for Eve and click Login
        composeTestRule.onNodeWithText("Enter PIN").performTextInput(evePin)
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify that the onLoginSuccess callback was invoked with the correct UserData
        verify(mockOnLoginSuccess).invoke(UserData("Eve", "Brown", R.drawable.eve_brown))
    }

    @Test
    fun testUnsuccessfulLogin() {
        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Enter an incorrect PIN and click Login
        composeTestRule.onNodeWithText("Enter PIN").performTextInput("000000")
        composeTestRule.onNodeWithText("Log In").performClick()

        // Verify that the onLoginSuccess callback was not invoked
        verify(mockOnLoginSuccess, never()).invoke(any())
    }

    @Test
    fun testPostNavigateIconButton() {
        composeTestRule.setContent {
            PracticeTheme {
                PinLoginScreen(
                    pinViewModel = mockPinViewModel,
                    onLoginSuccess = mockOnLoginSuccess,
                    onNavigate = mockOnNavigate,
                    onPostNavigate = mockOnPostNavigate
                )
            }
        }

        // Click the forward icon button to navigate
        composeTestRule.onNodeWithContentDescription("go to post").performClick()

        // Verify that the onPostNavigate callback was invoked
        verify(mockOnPostNavigate).invoke()
    }
}
