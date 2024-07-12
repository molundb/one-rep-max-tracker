package net.martinlundberg.a1repmaxtracker.feature.movementslist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
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
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    )
                )
            )
        }

        // Then
        composeTestRule.onNodeWithText("Test movement").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 kg").assertIsDisplayed()
    }

    @Test
    fun whenAddButtonIsClicked_thenAddMovementDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.POUNDS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    )
                )
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Add movement").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add movement dialog").assertIsDisplayed()
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenAddButtonIsClicked_thenDialogIsClosedAndNewMovementIsAdded() {
        // Given
        var addMovementCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                onAddMovementClick = { _, _ ->
                    addMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add movement").performClick()
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
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.POUNDS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                )
            )
        }
        composeTestRule.onNodeWithContentDescription("Add movement").performClick()

        // Then
        composeTestRule.onNodeWithText("Add").assertIsNotEnabled()
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenDismissButtonIsClicked_thenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        var addMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                onAddMovementClick = { _, _ ->
                    addMovementCalled = true
                }
            )
        }
        composeTestRule.onNodeWithContentDescription("Add movement").performClick()
        composeTestRule.onNodeWithContentDescription("Movement name text field").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add movement dialog").assertDoesNotExist()
        assertFalse(addMovementCalled)
    }

    @Test
    fun whenMovementIsClicked_thenNavigateToMovementDetailScreen() {
        // Given
        var onMovementClickCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    )
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
    fun whenMovementIsLongClicked_thenDropDownMenuIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    )
                ),
            )
        }

        // When
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // Then
        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
    }

    @Test
    fun givenMovementDropDownMenu_whenClickOutsideOfMenu_thenMenuIsDismissed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    )
                ),
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // When
        Espresso.pressBack()

        // Then
        composeTestRule.onNodeWithText("Edit").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsNotDisplayed()
    }

    @Test
    fun givenMovementDropDownMenu_whenDeleteIsClicked_thenMenuIsDismissedAndConfirmationDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    )
                ),
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // When
        composeTestRule.onNodeWithText("Delete").performClick()

        // Then
        composeTestRule.onNodeWithText("Edit").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertIsDisplayed()
    }

    @Test
    fun givenDeleteMovementConfirmDialog_whenConfirmIsClicked_thenMenuIsDismissedAndMovementIsDeleted() {
        // Given
        var deleteMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(
                            id = 17,
                            name = "Test movement",
                            weight = 3f
                        )
                    )
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
    fun givenMovementDropDownMenu_whenEditIsClicked_thenMenuIsDismissedAndEditDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    )
                ),
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // When
        composeTestRule.onNodeWithText("Edit").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Movement drop down menu").assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertIsDisplayed()
    }

    @Test
    fun givenMovementDropDownMenuDismissed_whenMovementLongClicked_thenDropDownMenuIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                weightUnit = WeightUnit.KILOGRAMS,
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    )
                ),
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
        Espresso.pressBack()

        // When
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // Then
        composeTestRule.onNodeWithText("Edit").assertIsDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsDisplayed()
    }

    /*
    // TODO: How to test this?
    @Test
    fun givenMovementDropDownMenuDismissed_whenNavigatingToDifferentScreenAndBack_thenDropDownMenuIsNotDisplayed() {
        // Given
        composeTestRule.setContent {
//            Navigation()
//            MovementsListScreen(
//                movementsListUiState = MovementsListUiState.Success(
//                    listOf(
//                        Movement(name = "Test movement",  weight = 3)
//                    )
//                ),
//                onMovementClick =
//            )
        }
//        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
//        Espresso.pressBack()

        // When
        composeTestRule.onNodeWithText("Test movement").performClick()
        Espresso.pressBack()

        // Then
        composeTestRule.onNodeWithText("Edit").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Delete").assertIsNotDisplayed()
    }
    */
}