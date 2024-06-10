package net.martinlundberg.a1repmaxtracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import net.martinlundberg.a1repmaxtracker.ui.HomeUiState
import net.martinlundberg.a1repmaxtracker.ui.Movement
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenListWithMovement_whenMainScreen_ThenMovementInfoIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState.Success(
                    listOf(
                        Movement("Test movement", 2)
                    )
                )
            )
        }

        // When
        composeTestRule.onNodeWithText("Test movement").assertIsDisplayed()

        // Then
        composeTestRule.onNodeWithText("2 kg").assertIsDisplayed()
    }

    @Test
    fun whenAddButtonIsPressed_ThenAddMovementDialogIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState.Success(
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

    /*
    Should integration tests be done and if so how? NIA app does not seem to have any.
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun givenAddMovementDialogWithMovementName_whenAddButtonIsPressed_ThenDialogIsClosedAndNewMovementIsDisplayed() {
        // Given
        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState.Success(
                    listOf()
                )
            )

        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()
        composeTestRule.onNodeWithText("Name of exercise").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Add").performClick()

        // Then
        composeTestRule.waitUntilExactlyOneExists(hasText("movement test name"))
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertDoesNotExist()

    }
     */

    @Test
    fun givenAddMovementDialogWithMovementName_whenAddButtonIsPressed_ThenDialogIsClosedAndNewMovementIsAdded() {
        // Given
        var addMovementCalled = false

        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState.Success(
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
    fun givenAddMovementDialogWithoutMovementName_whenAddButtonIsPressed_ThenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState.Success(
                    listOf()
                )
            )
        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()

        // When
        composeTestRule.onNodeWithText("Add").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Movement Card").assertDoesNotExist()
    }

    @Test
    fun givenAddMovementDialogWithMovementName_whenDismissButtonIsPressed_ThenDialogIsClosedAndNoNewMovementIsAdded() {
        // Given
        composeTestRule.setContent {
            MainScreen(
                homeUiState = HomeUiState.Success(
                    listOf()
                )
            )
        }
        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()
        composeTestRule.onNodeWithText("Name of exercise").performTextInput("movement test name")

        // When
        composeTestRule.onNodeWithText("Dismiss").performClick()

        // Then
        composeTestRule.onNodeWithContentDescription("Add Movement Dialog").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Movement Card").assertDoesNotExist()
    }
}