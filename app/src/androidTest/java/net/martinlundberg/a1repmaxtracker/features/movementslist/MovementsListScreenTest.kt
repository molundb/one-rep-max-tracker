package net.martinlundberg.a1repmaxtracker.features.movementslist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementsListScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenListWithMovement_thenMovementInfoIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement("Test movement", 2)
                    )
                )
            )
        }

        // Then
        composeTestRule.onNodeWithText("Test movement").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 kg").assertIsDisplayed()
    }

    @Test
    fun whenAddButtonIsPressed_thenAddMovementDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement("Test movement", 2)
                    )
                )
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertIsDisplayed()
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenAddButtonIsPressed_thenDialogIsClosedAndNewMovementIsAdded() {
        // Given
        var addMovementCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                addMovement = {
                    addMovementCalled = true
                }
            )

        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()
        composeTestRule.onNodeWithText("Name of exercise").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Add").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertDoesNotExist()
        assertTrue(addMovementCalled)
    }

    @Test
    fun givenAddMovementDialogWithoutMovementName_whenAddButtonIsPressed_thenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        var addMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                addMovement = {
                    addMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()

        // When
        composeTestRule.onNodeWithText("Add").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertDoesNotExist()
        assertFalse(addMovementCalled)
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenDismissButtonIsPressed_thenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        var addMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                addMovement = {
                    addMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()
        composeTestRule.onNodeWithText("Name of exercise").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Dismiss").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertDoesNotExist()
        assertFalse(addMovementCalled)
    }

    @Test
    fun whenMovementIsPressed_thenNavigateToMovementDetailScreen() {
        // Given
        var onMovementClickCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement("Test movement", 3)
                    )
                ),
                onMovementClick = { onMovementClickCalled = true }
            )
        }

        // When
        composeTestRule.onNodeWithText("Test movement").performClick()

        // Then
        assertTrue(onMovementClickCalled)
    }
}