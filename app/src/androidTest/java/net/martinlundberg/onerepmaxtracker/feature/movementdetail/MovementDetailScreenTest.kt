package net.martinlundberg.onerepmaxtracker.feature.movementdetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Locale
import java.util.TimeZone

@RunWith(AndroidJUnit4::class)
class MovementDetailScreenTest {

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
    fun givenStateIsLoading_thenLoadingIndicatorAndMovementNameAreDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 15,
                movementDetailUiState = MovementDetailUiState.Loading(MovementDetail("The name")),
            )
        }
        composeTestRule.onNodeWithText("The name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
    }

    @Test
    fun givenResults_thenInfoIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 2,

                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        movementName = "Name",
                        listOf(
                            Result(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                                dateTimeFormatted = "Jun 10, 2024",
                                comment = "",
                            ),
                            Result(
                                id = 2,
                                movementId = 2,
                                weight = 72f,
                                offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC),
                                dateTimeFormatted = "Jul 17, 2024",
                                comment = "",
                            ),
                            Result(
                                id = 3,
                                movementId = 2,
                                weight = 75f,
                                offsetDateTime = OffsetDateTime.of(2024, 8, 28, 0, 0, 0, 0, ZoneOffset.UTC),
                                dateTimeFormatted = "Aug 28, 2024",
                                comment = "Comment",
                            ),
                        ),
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
            )
        }

        composeTestRule.onNodeWithText("70 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jun 10, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("72 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jul 17, 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("75 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aug 28, 2024").assertIsDisplayed()
    }

    @Test
    fun whenEditButtonIsClicked_thenEditMovementDialogIsDisplayed() {
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

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertIsDisplayed()
    }

    // TODO: Update
    @Test
    @Ignore
    fun givenEditDialog_whenDateIsClicked_thenCalendarDialogIsDisplayed() {
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
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Edit movement button").performClick()
        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()
    }

    // TODO: Update
    @Test
    @Ignore
    fun givenCalendarDialog_whenDateIsSelectedAndAcceptButtonIsClicked_thenDialogIsClosedAndAddResultIsCalled() {
        var addResultCalled = false
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
                addResult = { _, _ ->
                    addResultCalled = true
                },
            )
        }

        composeTestRule.onNodeWithContentDescription("Edit movement button").performClick()
        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()
        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()

        composeTestRule.onNodeWithText("Wednesday, July 17, 2024").performClick()
        composeTestRule.onNodeWithText("Accept").performClick()
        composeTestRule.onNodeWithContentDescription("Edit result button").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").assertDoesNotExist()
        assertTrue(addResultCalled)
    }
}