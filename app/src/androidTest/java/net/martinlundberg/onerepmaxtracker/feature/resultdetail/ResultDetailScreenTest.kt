package net.martinlundberg.onerepmaxtracker.feature.resultdetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.Percentage
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.resultdetail.ResultDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.resultdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService.WeightUnit
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

        composeTestRule.onNodeWithContentDescription("Edit result dialog").assertIsDisplayed()
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

        composeTestRule.onNodeWithContentDescription("Delete result confirmation dialog").assertIsDisplayed()
    }
}