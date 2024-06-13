package net.martinlundberg.a1repmaxtracker.features.movementdetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementDetailScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenStateIsSuccess_thenNameOfMovementIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name of movement")
                )
            )
        }

        composeTestRule.onNodeWithText("Name of movement").assertIsDisplayed()
    }

    @Test
    fun givenStateIsLoading_thenLoadingIndicatorAndTextAreDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Loading
            )
        }

        composeTestRule.onNodeWithText("Loading").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular Progress Indicator").assertIsDisplayed()
    }

    @Test
    fun givenListOf1RMs_thenListInfoIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        "Name of movement", listOf(
                            OneRMInfo(70, "10 Jun, 2024"),
                            OneRMInfo(72, "17 Jun, 2024"),
                            OneRMInfo(75, "28 Jun, 2024"),
                        )
                    )
                )
            )
        }

        composeTestRule.onNodeWithText("70").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 Jun, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("72").assertIsDisplayed()
        composeTestRule.onNodeWithText("17 Jun, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("75").assertIsDisplayed()
        composeTestRule.onNodeWithText("28 Jun, 2024").assertIsDisplayed()
    }

    @Test
    fun givenAdd1RMButtonIsPressed_thenAdd1RMDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Loading
            )
        }

    }

    @Test
    fun givenAdd1RMButtonIsPressed_whenAddButtonIsPressed_thenDialogIsClosedAndNew1RMIsAdded() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Loading
            )
        }

    }
}