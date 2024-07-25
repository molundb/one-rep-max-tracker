package net.martinlundberg.onerepmaxtracker.feature.dialogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailScreen
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class ResultDialogsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenAddResultButtonIsClicked_thenAddResultDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.POUNDS,
                )
            )
        }

        composeTestRule.onNodeWithText("+ Add new").performClick()

        composeTestRule.onNodeWithContentDescription("Add result dialog").assertIsDisplayed()
    }

    @Test
    fun givenAddResultDialogWithNoWeight_thenAddButtonIsDisabled() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.POUNDS,
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()

        composeTestRule.onNodeWithText("Add result").assertIsNotEnabled()
    }

    @Test
    fun givenAddResultDialogWithWeight_whenAddButtonIsClicked_thenDialogIsClosedAndResultIsAdded() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                addResult = { _, _ ->
                    addResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()
        composeTestRule.onNodeWithContentDescription("Weight text field").performTextInput("780")

        composeTestRule.onNodeWithText("Add result").performClick()

        composeTestRule.onNodeWithContentDescription("Add result dialog").assertDoesNotExist()
        assertTrue(addResultCalled)
    }

    @Test
    fun givenAddResultDialogWithWeight_whenDismissButtonIsClicked_thenDialogIsClosedAndNoResultIsAdded() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                addResult = { _, _ ->
                    addResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()
        composeTestRule.onNodeWithContentDescription("Weight text field").performTextInput("780")

        composeTestRule.onNodeWithText("Add result").performClick()

        composeTestRule.onNodeWithContentDescription("Add result dialog").assertDoesNotExist()
        assertTrue(addResultCalled)
    }

    @Test
    fun givenEditMovementDialog_whenDeleteMovementIsClicked_thenDeleteMovementConfirmationDialogIsDisplayed() {
        var deleteCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        "Back Squat",
                        listOf(
                            Result(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                                comment = "",
                            )
                        )
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onDeleteResultClick = {
                    deleteCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Edit movement button").performClick()
        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Delete movement").performClick()

        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertIsDisplayed()
        assertFalse(deleteCalled)
    }
}