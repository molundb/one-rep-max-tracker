package net.martinlundberg.onerepmaxtracker.feature.movementlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.POUNDS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementListScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenLoading_thenLoadingIndicatorIsDisplayed() {
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Loading,
            )
        }

        composeTestRule.onNodeWithText("Your top results").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Latest or best results switch").assertDoesNotExist()
    }

    @Test
    fun givenEmptyMovementList_thenEmptyStateIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = true,
                    showBestResults = true,
                )
            )
        }

        // Then
        composeTestRule.onNodeWithText("Your top results").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start by adding your first result").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Latest or best results switch").assertDoesNotExist()
    }

    @Test
    fun givenListWithMovement_thenMovementInfoIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = true,
                    showBestResults = true,
                )
            )
        }

        // Then
        composeTestRule.onNodeWithText("Your top results").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Latest or best results switch").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test movement").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 kg").assertIsDisplayed()
    }

    @Test
    fun whenLatestOrBestResultSwitchChecked_thenOnLatestOrBestResultsSwitchCheckedCalled() {
        // Given
        var onLatestOrBestResultsSwitchChecked = false

        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = true,
                    showBestResults = true,
                ),
                onLatestOrBestResultsSwitchChecked = {
                    onLatestOrBestResultsSwitchChecked = true
                }
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Latest or best results switch").performClick()

        // Then
        assertTrue(onLatestOrBestResultsSwitchChecked)
    }

    @Test
    fun whenMovementIsClicked_thenOnMovementClickCalled() {
        // Given
        var onMovementClickCalled = false

        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = true,
                    showBestResults = true,
                ),
                onMovementClick = { _, _ -> onMovementClickCalled = true }
            )
        }

        // When
        composeTestRule.onNodeWithText("Test movement").performClick()

        // Then
        assertTrue(onMovementClickCalled)
    }

    @Test
    fun whenAddMovementButtonIsClicked_thenAddMovementDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    ),
                    weightUnit = POUNDS,
                    isAnalyticsEnabled = true,
                    showBestResults = true,
                )
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Add movement button").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add movement dialog").assertIsDisplayed()
    }

    @Test
    fun whenMovementIsLongClicked_thenDropDownMenuIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                    showBestResults = true,
                ),
            )
        }

        // When
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // Then
        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
    }
}