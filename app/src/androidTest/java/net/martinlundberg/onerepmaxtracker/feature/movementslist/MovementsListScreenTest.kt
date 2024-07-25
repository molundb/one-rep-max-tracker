package net.martinlundberg.onerepmaxtracker.feature.movementslist

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
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementsListScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenStateIsLoading_thenLoadingIndicatorIsDisplayed() {
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Loading,
            )
        }

        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
    }

    @Test
    fun givenListWithMovement_thenMovementInfoIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = true,
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    ),
                    weightUnit = WeightUnit.POUNDS,
                    isAnalyticsEnabled = true,
                )
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Add movement button").performClick()

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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
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
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(),
                    weightUnit = WeightUnit.POUNDS,
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
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
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
    fun whenMovementIsClicked_thenNavigateToMovementDetailScreen() {
        // Given
        var onMovementClickCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = true,
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = false,
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = false,
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = false,
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(
                            id = 17,
                            name = "Test movement",
                            weight = 3f
                        )
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
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
    fun givenMovementDropDownMenu_whenEditIsClicked_thenMenuIsDismissedAndEditDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = false,
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
    fun givenEditMovementDialog_whenSaveClicked_thenDialogIsClosedAndMovementIsEdited() {
        // Given
        var editMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
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
    fun givenEditMovementDialog_whenDeleteClicked_thenDeleteMovementConfirmationDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
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
    fun givenEditMovementDialog_whenCancelClicked_thenEditDialogIsClosedAndMovementIsNotEdited() {
        // Given
        var editMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
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
    fun givenMovementDropDownMenuDismissed_whenMovementLongClicked_thenDropDownMenuIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                innerPadding = PaddingValues(),
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    isAnalyticsEnabled = false,
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