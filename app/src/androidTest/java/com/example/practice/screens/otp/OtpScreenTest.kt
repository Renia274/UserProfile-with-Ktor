package com.example.practice.screens.otp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.profiles.viewmodel.SharedProfilesViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewModel
import com.example.practice.profiles.viewmodel.otp.FirebaseOtpViewState
import com.example.practice.ui.theme.PracticeTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class OtpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockSharedViewModel = mock<SharedProfilesViewModel>()
    private val mockOtpViewModel = mock<FirebaseOtpViewModel>()
    private val email = "example@email.com"
    private val signupEmail = "example@email.com"
    private val generatedOtp = "123456"

    private val viewStateFlow = MutableStateFlow(FirebaseOtpViewState())

    @Test
    fun testOtpGenerationWithMatchingEmail() {
        // Setup the mock behavior
        doReturn(generatedOtp).`when`(mockOtpViewModel).createOtp(email, signupEmail)
        viewStateFlow.value = viewStateFlow.value.copy(codeSentMessage = "OTP created successfully, please verify the code")

        composeTestRule.setContent {
            PracticeTheme {
                OtpScreen(
                    onNavigate = {},
                    onBackPressed = {},
                    viewModel = mockSharedViewModel,
                    otpViewModel = mockOtpViewModel
                )
            }
        }

        // Enter valid email and OTP
        composeTestRule.onNodeWithText("Enter Email Address").performTextInput(email)
        composeTestRule.onNodeWithText("Generate OTP").performClick()

        // Verify OTP generation
        composeTestRule.onNodeWithText("OTP created successfully, please verify the code").assertIsDisplayed()

        // Verify that the OTP was set
        verify(mockOtpViewModel).createOtp(email, signupEmail)
    }

    @Test
    fun testEmailErrorMessageDisplay() {
        // Setup the mock behavior
        viewStateFlow.value = viewStateFlow.value.copy(emailErrorMessage = "Entered email doesn't match the signup email")

        composeTestRule.setContent {
            PracticeTheme {
                OtpScreen(
                    onNavigate = {},
                    onBackPressed = {},
                    viewModel = mockSharedViewModel,
                    otpViewModel = mockOtpViewModel
                )
            }
        }

        // Enter an invalid email
        composeTestRule.onNodeWithText("Enter Email Address").performTextInput("invalid@email.com")
        composeTestRule.onNodeWithText("Generate OTP").performClick()

        // Verify email error message
        composeTestRule.onNodeWithText("Entered email doesn't match the signup email").assertIsDisplayed()
    }

    @Test
    fun testOtpVerificationSuccess() {
        viewStateFlow.value = viewStateFlow.value.copy(isOtpVerified = true)

        composeTestRule.setContent {
            PracticeTheme {
                OtpScreen(
                    onNavigate = {},
                    onBackPressed = {},
                    viewModel = mockSharedViewModel,
                    otpViewModel = mockOtpViewModel
                )
            }
        }

        // Enter valid email and OTP
        composeTestRule.onNodeWithText("Enter Email Address").performTextInput(email)
        composeTestRule.onNodeWithText("Enter OTP").performTextInput("123456")
        composeTestRule.onNodeWithText("Verify OTP").performClick()

        // Verify OTP verification
        composeTestRule.onNodeWithText("OTP created successfully, please verify the code").assertDoesNotExist()
    }

    @Test
    fun testOtpVerificationFailure() {
        // Setup the mock behavior
        viewStateFlow.value = viewStateFlow.value.copy(verificationErrorMessage = "Verification failed: Invalid OTP")

        composeTestRule.setContent {
            PracticeTheme {
                OtpScreen(
                    onNavigate = {},
                    onBackPressed = {},
                    viewModel = mockSharedViewModel,
                    otpViewModel = mockOtpViewModel
                )
            }
        }

        // Enter valid email but incorrect OTP
        composeTestRule.onNodeWithText("Enter Email Address").performTextInput(email)
        composeTestRule.onNodeWithText("Enter OTP").performTextInput("wrongOtp")
        composeTestRule.onNodeWithText("Verify OTP").performClick()

        // Verify verification error message
        composeTestRule.onNodeWithText("Verification failed: Invalid OTP").assertIsDisplayed()
    }

    @Test
    fun testBackNavigation() {
        // Create a mock callback
        val mockOnBackPressed = mock<() -> Unit>()
        composeTestRule.setContent {
            PracticeTheme {
                OtpScreen(
                    onNavigate = {},
                    onBackPressed = mockOnBackPressed,
                    viewModel = mockSharedViewModel,
                    otpViewModel = mockOtpViewModel
                )
            }
        }

        // Click the back button
        composeTestRule.onNodeWithContentDescription("Back").performClick()

        // Verify that the back navigation callback was invoked
        verify(mockOnBackPressed).invoke()
    }

    @Test
    fun testInitialUIRendering() {
        composeTestRule.setContent {
            PracticeTheme {
                OtpScreen(
                    onNavigate = {},
                    onBackPressed = {},
                    viewModel = mockSharedViewModel,
                    otpViewModel = mockOtpViewModel
                )
            }
        }

        // Check that initial UI components are displayed
        composeTestRule.onNodeWithText("Enter Email Address").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter OTP").assertIsDisplayed()
        composeTestRule.onNodeWithText("Generate OTP").assertIsDisplayed()
        composeTestRule.onNodeWithText("Verify OTP").assertIsDisplayed()
    }
}
