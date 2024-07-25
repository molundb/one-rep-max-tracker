package net.martinlundberg.onerepmaxtracker.feature.movementlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.POUNDS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementListScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenStateIsLoading_thenLoadingIndicatorIsDisplayed() {
        composeTestRule.setContent {
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Loading,
            )
        }

        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
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
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 2f)
                    ),
                    weightUnit = POUNDS,
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
    fun whenMovementIsClicked_thenNavigateToMovementDetailScreen() {
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
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
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
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
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
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
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
    fun givenMovementDropDownMenu_whenEditIsClicked_thenMenuIsDismissedAndEditDialogIsDisplayed() {
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
            MovementListScreen(
                innerPadding = PaddingValues(),
                movementListUiState = Success(
                    listOf(
                        Movement(name = "Test movement", weight = 3f)
                    ),
                    weightUnit = KILOGRAMS,
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