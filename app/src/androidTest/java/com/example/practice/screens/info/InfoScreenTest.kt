package com.example.practice.screens.info

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.practice.ui.theme.PracticeTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class InfoScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockOnNavigateBack = mock<() -> Unit>()
    private val mockOnOpenLink = mock<() -> Unit>()

    @Test
    fun testOpenLinkNavigation() {
        composeTestRule.setContent {
            PracticeTheme {
                InfoScreenContent(
                    onNavigateBack = mockOnNavigateBack,
                    onOpenLink = mockOnOpenLink
                )
            }
        }

        // Verify that the "Open Link" button is displayed and clickable
        composeTestRule.onNodeWithText("Open Link").assertIsDisplayed().performClick()

        // Verify that the onOpenLink callback was invoked
        verify(mockOnOpenLink).invoke()
    }
}
