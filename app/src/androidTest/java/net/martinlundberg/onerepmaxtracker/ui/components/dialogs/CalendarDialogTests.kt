package net.martinlundberg.onerepmaxtracker.ui.components.dialogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.resultdetail.ResultDetailScreen
import net.martinlundberg.onerepmaxtracker.feature.resultdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class CalendarDialogTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenCalendarDialog_whenCancel_thenEditDialogIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 25, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    percentagesOf1RM = emptyList(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
            )
        }
        composeTestRule.onNodeWithText("Edit").performClick()
        composeTestRule.onNodeWithText("25 Jul 2024").performClick()

        composeTestRule.onNodeWithContentDescription("Date picker dialog").assertIsDisplayed()

        composeTestRule.onAllNodesWithText("Cancel")
            .filterToOne(hasAnyAncestor(hasContentDescriptionExactly("Date picker dialog")))
            .performClick()

        composeTestRule.onNodeWithContentDescription("Date picker dialog").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Edit result dialog").assertIsDisplayed()
    }

    @Test
    fun givenCalendarDialog_whenDateSelectedAndConfirm_thenSelectedDateIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 25, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "",
                    ),
                    percentagesOf1RM = emptyList(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
            )
        }
        composeTestRule.onNodeWithText("Edit").performClick()
        composeTestRule.onNodeWithText("25 Jul 2024").performClick()

        composeTestRule.onNodeWithText("Wednesday, July 17, 2024").performClick()
        composeTestRule.onNodeWithText("Accept").performClick()

        composeTestRule.onNodeWithContentDescription("Date picker dialog").assertDoesNotExist()
        composeTestRule.onNodeWithContentDescription("Edit result dialog").assertIsDisplayed()
        composeTestRule.onNodeWithText("17 Jul 2024").assertIsDisplayed()
    }
}