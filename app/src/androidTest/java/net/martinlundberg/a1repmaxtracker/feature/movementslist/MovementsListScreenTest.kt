package net.martinlundberg.a1repmaxtracker.feature.movementslist

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
                        Movement(name = "Test movement", weight = 2)
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2)
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
    fun givenAddMovementDialogWithMovementName_whenAddButtonIsClicked_thenDialogIsClosedAndNewMovementIsAdded() {
        // Given
        var addMovementCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                onAddMovementClick = {
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
    fun givenAddMovementDialogWithoutMovementName_thenAddButtonIsDisabled() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                )
            )
        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()

        // Then
        composeTestRule.onNodeWithText("Add").assertIsNotEnabled()
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenDismissButtonIsClicked_thenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        var addMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf()
                ),
                onAddMovementClick = {
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
    fun whenMovementIsClicked_thenNavigateToMovementDetailScreen() {
        // Given
        var onMovementClickCalled = false

        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3)
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

    @Test
    fun whenMovementIsLongClicked_thenDropDownMenuIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3)
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3)
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
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3)
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
        composeTestRule.onNodeWithContentDescription("Delete Movement Confirmation Dialog").assertIsDisplayed()
    }

    @Test
    fun givenDeleteMovementConfirmDialog_whenConfirmIsClicked_thenMenuIsDismissedAndMovementIsDeleted() {
        // Given
        var deleteMovementCalled = false
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(
                            id = 17,
                            name = "Test movement",
                            weight = 3
                        )
                    )
                ),
                onDeleteMovementClick = { deleteMovementCalled = true }
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }
        composeTestRule.onNodeWithText("Delete").performClick()

        // When
        composeTestRule.onNodeWithText("Confirm").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Delete Movement Confirmation Dialog").assertDoesNotExist()
        assertTrue(deleteMovementCalled)
    }

    @Test
    fun givenMovementDropDownMenu_whenEditIsClicked_thenMenuIsDismissedAndEditDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(id = 2, name = "Test movement", weight = 3)
                    )
                ),
            )
        }
        composeTestRule.onNodeWithText("Test movement").performTouchInput { longClick() }

        // When
        composeTestRule.onNodeWithText("Edit").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Movement Drop Down Menu").assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription("Edit Movement Dialog").assertIsDisplayed()
    }

    @Test
    fun givenMovementDropDownMenuDismissed_whenMovementLongClicked_thenDropDownMenuIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MovementsListScreen(
                movementsListUiState = MovementsListUiState.Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3)
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