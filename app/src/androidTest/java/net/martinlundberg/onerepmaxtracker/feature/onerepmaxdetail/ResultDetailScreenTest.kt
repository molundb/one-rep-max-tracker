package net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.Percentage
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class ResultDetailScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenLoading_thenLoadingIndicatorAndMovementNameAreDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Loading,
            )
        }

        composeTestRule.onNodeWithText("Name of movement").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
    }

    @Test
    fun givenResult_thenResultInfoIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        dateTimeFormatted = "Jul 17, 2024",
                        comment = "",
                    ),
                    percentagesOf1RM = listOf(
                        Percentage(90, 90),
                        Percentage(85, 85),
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("100 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jul 17, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("Percentages of 1RM").assertIsDisplayed()
        composeTestRule.onNodeWithText("90 %").assertIsDisplayed()
        composeTestRule.onNodeWithText("90 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("85 %").assertIsDisplayed()
        composeTestRule.onNodeWithText("85 kg").assertIsDisplayed()
    }

    @Test
    fun whenNavBackButtonIsClicked_thenNavigateBack() {
        var navigateBackCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        dateTimeFormatted = "Jul 17, 2024",
                        comment = "",
                    ),
                    percentagesOf1RM = listOf(
                        Percentage(90, 90),
                        Percentage(85, 85),
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                navigateBack = {
                    navigateBackCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Back button").performClick()
        assertTrue(navigateBackCalled)
    }

    @Test
    fun whenEditIsClicked_thenEditResultDialogIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithContentDescription("Edit result dialog").isDisplayed()
    }

    @Test
    fun whenDeleteIsClicked_thenDeleteResultConfirmDialogIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("Delete").performClick()

        composeTestRule.onNodeWithContentDescription("Delete result confirmation dialog").isDisplayed()
    }


    @Test
    @Ignore
    fun givenCalendarDialog_whenCancelButtonIsClicked_thenDialogIsClosedAndUpdateIsNotCalled() {
        var updateOneRepMaxDetailCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    percentagesOf1RM = emptyList(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onEditResultClick = { _, _ ->
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
    @Ignore
    fun givenCalendarDialog_whenAcceptButtonIsClicked_thenDialogIsClosedAndUpdateIsCalled() {
        var updateOneRepMaxDetailCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    percentagesOf1RM = emptyList(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onEditResultClick = { _, _ ->
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
}