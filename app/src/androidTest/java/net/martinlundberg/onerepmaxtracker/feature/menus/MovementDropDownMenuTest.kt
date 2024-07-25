package net.martinlundberg.onerepmaxtracker.feature.menus

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
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListScreen
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovementDropDownMenuTest {

    @get:Rule
    val composeTestRule = createComposeRule()

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
}