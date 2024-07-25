package net.martinlundberg.onerepmaxtracker.feature.dialogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailScreen
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListScreen
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.POUNDS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementDialogsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenAddMovementDialogWithMovementName_whenAddButtonIsClicked_thenDialogIsClosedAndNewMovementIsAdded() {
        // Given
        var addMovementCalled = false

        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
                onAddMovementClick = { _, _ ->
                    addMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add movement button").performClick()
        composeTestRule.onNodeWithContentDescription("Movement name text field").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Add").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add movement dialog").assertDoesNotExist()
        assertTrue(addMovementCalled)
    }

    @Test
    fun givenAddMovementDialogWithoutMovementName_thenAddButtonIsDisabled() {
        // Given
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(),
                    weightUnit = POUNDS,
                    isAnalyticsEnabled = false,
                )
            )
        }
        composeTestRule.onNodeWithContentDescription("Add movement button").performClick()

        // Then
        composeTestRule.onNodeWithText("Add").assertIsNotEnabled()
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenCancelButtonIsClicked_thenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        var addMovementCalled = false
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
                onAddMovementClick = { _, _ ->
                    addMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add movement button").performClick()
        composeTestRule.onNodeWithContentDescription("Movement name text field").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add movement dialog").assertDoesNotExist()
        assertFalse(addMovementCalled)
    }

    @Test
    fun givenEditMovementDialog_whenSaveClicked_thenDialogIsClosedAndMovementIsEdited() {
        // Given
        var editMovementCalled = false
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
                onEditMovementClick = {
                    editMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Edit").performClick()

        // When
        composeTestRule.onNodeWithText("Save").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertDoesNotExist()
        assertTrue(editMovementCalled)
    }

    @Test
    fun givenEditMovementDialog_whenCancelClicked_thenEditDialogIsClosedAndMovementIsNotEdited() {
        // Given
        var editMovementCalled = false
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
                onEditMovementClick = {
                    editMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Edit").performClick()

        // When
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertDoesNotExist()
        assertFalse(editMovementCalled)
    }

    @Test
    fun givenEditMovementDialog_whenDeleteClicked_thenDeleteMovementConfirmationDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Edit").performClick()

        // When
        composeTestRule.onNodeWithText("Delete movement").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertIsDisplayed()
    }

    @Test
    fun givenMovementListScreenWithDeleteMovementConfirmDialog_whenConfirmIsClicked_thenMenuIsDismissedAndMovementIsDeleted() {
        // Given
        var deleteMovementCalled = false
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(
                            id = 17,
                            name = "Test movement",
                            weight = 3f
                        )
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = true,
                ),
                onDeleteMovementClick = { deleteMovementCalled = true }
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Delete").performClick()

        // When
        composeTestRule.onNodeWithText("Yes, delete").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertDoesNotExist()
        assertTrue(deleteMovementCalled)
    }

    @Test
    fun givenMovementDetailScreenWithDeleteMovementConfirmDialog_whenConfirmIsClicked_thenMenuIsDismissedAndMovementIsDeleted() {
        var deleteMovementCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = POUNDS,
                ),
                onDeleteMovementClick = { deleteMovementCalled = true }
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()
        composeTestRule.onNodeWithText("Delete movement").performClick()
        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Yes, delete").performClick()

        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertDoesNotExist()
        assertTrue(deleteMovementCalled)
    }
}