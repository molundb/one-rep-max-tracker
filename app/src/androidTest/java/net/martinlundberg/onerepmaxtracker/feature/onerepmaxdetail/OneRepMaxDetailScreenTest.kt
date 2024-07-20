package net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class OneRepMaxDetailScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenMovementName_thenNameOfMovementIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
            )
        }

        composeTestRule.onNodeWithText("Name of movement").assertIsDisplayed()
    }

    @Test
    fun givenStateIsLoading_thenLoadingIndicatorAndTextAreDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Loading(weightUnit = WeightUnit.KILOGRAMS),
            )
        }

        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
    }

    @Test
    fun givenResult_thenInfoIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("Weight").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Outlined Text Field Time Picker").assertIsDisplayed()
    }

    @Test
    fun whenDateIsClicked_thenCalendarDialogIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()
    }

    @Test
    fun givenCalendarDialog_whenCancelButtonIsClicked_thenDialogIsClosedAndUpdateIsNotCalled() {
        var updateOneRepMaxDetailCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                updateResultDetail = {
                    updateOneRepMaxDetailCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()
        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").assertDoesNotExist()
        assertFalse(updateOneRepMaxDetailCalled)
    }

    @Test
    fun givenCalendarDialog_whenAcceptButtonIsClicked_thenDialogIsClosedAndUpdateIsCalled() {
        var updateOneRepMaxDetailCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                updateResultDetail = {
                    updateOneRepMaxDetailCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()
        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()

        composeTestRule.onNodeWithText("Wednesday, July 17, 2024").performClick()
        composeTestRule.onNodeWithText("Accept").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").assertDoesNotExist()
        assertTrue(updateOneRepMaxDetailCalled)
    }

    @Test
    fun whenTimeIsClicked_thenTimePickerDialogIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Time Picker").performClick()

        composeTestRule.onNodeWithContentDescription("Time Picker Dialog").isDisplayed()
    }

    @Test
    fun givenTimePickerDialog_whenCancelButtonIsClicked_thenDialogIsClosedAndUpdateIsNotCalled() {
        var updateOneRepMaxDetailCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                updateResultDetail = {
                    updateOneRepMaxDetailCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Time Picker").performClick()
        composeTestRule.onNodeWithContentDescription("Time Picker Dialog").isDisplayed()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Time Picker Dialog").assertDoesNotExist()
        assertFalse(updateOneRepMaxDetailCalled)
    }

    @Test
    fun givenTimePickerDialog_whenAcceptButtonIsClicked_thenDialogIsClosedAndUpdateIsCalled() {
        var updateOneRepMaxDetailCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                resultId = 1,
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                updateResultDetail = {
                    updateOneRepMaxDetailCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Time Picker").performClick()
        composeTestRule.onNodeWithContentDescription("Time Picker Dialog").isDisplayed()

        composeTestRule.onNodeWithContentDescription("5 o'clock").performClick()
        composeTestRule.onNodeWithContentDescription("45 minutes").performClick()

        composeTestRule.onNodeWithText("Accept").performClick()

        composeTestRule.onNodeWithContentDescription("Time Picker Dialog").assertDoesNotExist()
        assertTrue(updateOneRepMaxDetailCalled)
    }
}