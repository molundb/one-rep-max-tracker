package net.martinlundberg.onerepmaxtracker.ui.components.dialogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasContentDescriptionExactly
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailScreen
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailScreen
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Locale
import java.util.TimeZone

@RunWith(AndroidJUnit4::class)
class ResultDialogsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        setLocalTo(Locale("en", "US"))
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    private fun setLocalTo(testLocale: Locale) {
        Locale.setDefault(testLocale)
        val config = InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration
        config.setLocale(testLocale)
        InstrumentationRegistry.getInstrumentation().targetContext.createConfigurationContext(config)
    }

    @Test
    fun givenAddResultDialog_thenDateFieldIsTodaysDate() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.POUNDS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()

        composeTestRule.onNodeWithContentDescription("Add result dialog").assertIsDisplayed()
        composeTestRule.onNodeWithText("05 Jan 2023").assertIsDisplayed()
    }

    @Test
    fun givenAddResultDialogWithNoWeight_thenConfirmButtonIsDisabled() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.POUNDS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()

        composeTestRule.onNodeWithText("Add result").assertIsNotEnabled()
    }

    @Test
    fun givenAddResultDialogWithWeight_whenConfirm_thenDialogIsClosedAndResultIsAdded() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.KILOGRAMS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
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
    fun givenAddResultDialogWithWeight_whenCancel_thenDialogIsClosedAndNoResultIsAdded() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.KILOGRAMS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                ),
                addResult = { _, _ ->
                    addResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()
        composeTestRule.onNodeWithContentDescription("Weight text field").performTextInput("780")

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Add result dialog").assertDoesNotExist()
        assertFalse(addResultCalled)
    }

    @Test
    fun whenEditResultDialog_thenResultInfoIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("100 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("17 Jul 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is a comment").assertIsDisplayed()
    }

    @Test
    fun whenEditResultDialog_whenCancel_thenDialogIsClosedAndResultIsNotEdited() {
        var editResultCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onEditResult = { _, _ ->
                    editResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("Cancel").performClick()
        assertFalse(editResultCalled)
    }

    @Test
    fun whenEditResultDialog_whenConfirm_thenDialogIsClosedAndResultIsEdited() {
        var editResultCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onEditResult = { _, _ ->
                    editResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("Save").performClick()
        assertTrue(editResultCalled)
    }

    @Test
    fun whenEditResultDialog_whenDelete_thenDeleteResultConfirmDialogIsDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("Delete result").performClick()

        composeTestRule.onNodeWithContentDescription("Delete result confirmation dialog").assertIsDisplayed()
    }

    @Test
    fun givenEditResultDialog_whenDateClicked_thenCalendarDialogIsDisplayed() {
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
                )
            )
        }
        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("25 Jul 2024").performClick()

        composeTestRule.onNodeWithContentDescription("Date picker dialog").assertIsDisplayed()
    }

    @Test
    fun givenDeleteResultConfirmDialog_thenMovementNameAndWeightAreDisplayed() {
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                )
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithText("Delete result").performClick()

        composeTestRule.onNodeWithText("Name of movement").assertIsDisplayed()
        composeTestRule.onNodeWithText("100 kg").assertIsDisplayed()
    }

    @Test
    fun givenDeleteResultConfirmDialog_whenCancel_thenDialogIsClosedAndResultIsNotDeleted() {
        var deleteResultCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onDeleteResult = {
                    deleteResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithText("Delete").performClick()

        composeTestRule.onNodeWithText("Cancel").performClick()

        composeTestRule.onNodeWithContentDescription("Delete result dialog").assertDoesNotExist()
        assertFalse(deleteResultCalled)
    }

    @Test
    fun givenDeleteResultConfirmDialog_whenConfirm_thenDialogIsClosedAndResultIsDeleted() {
        var deleteResultCalled = false
        composeTestRule.setContent {
            ResultDetailScreen(
                innerPadding = PaddingValues(),
                movementName = "Name of movement",
                resultDetailUiState = Success(
                    result = Result(
                        movementId = 55,
                        weight = 100f,
                        offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "This is a comment",
                    ),
                    percentagesOf1RM = listOf(),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
                onDeleteResult = {
                    deleteResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithText("Delete").performClick()

        composeTestRule.onNodeWithText("Yes, delete").performClick()

        composeTestRule.onNodeWithContentDescription("Delete result dialog").assertDoesNotExist()
        assertTrue(deleteResultCalled)
    }

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