package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertTrue
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class MovementDetailScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        setLocalTo(Locale("en", "US"))
    }

    private fun setLocalTo(testLocale: Locale) {
        Locale.setDefault(testLocale)
        val config = InstrumentationRegistry.getInstrumentation().targetContext.resources.configuration
        config.setLocale(testLocale)
        InstrumentationRegistry.getInstrumentation().targetContext.createConfigurationContext(config)
    }

    @Test
    fun givenMovementName_thenNameOfMovementIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 11,
                movementName = "Name of movement",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                ),
            )
        }

        composeTestRule.onNodeWithText("Name of movement").assertIsDisplayed()
    }

    @Test
    fun givenStateIsLoading_thenLoadingIndicatorIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 15,
                movementName = "The name",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Loading,
            )
        }

        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
    }

    @Test
    fun givenResults_thenInfoIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 2,
                movementName = "Name",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        listOf(
                            OneRMInfo(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC)
                            ),
                            OneRMInfo(
                                id = 2,
                                movementId = 2,
                                weight = 72f,
                                offsetDateTime = OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC)
                            ),
                            OneRMInfo(
                                id = 3,
                                movementId = 2,
                                weight = 75f,
                                offsetDateTime = OffsetDateTime.of(2024, 8, 28, 0, 0, 0, 0, ZoneOffset.UTC)
                            ),
                        )
                    )
                )
            )
        }

        composeTestRule.onNodeWithText("70 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("10 Jun 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("72 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("17 Jul 2024").assertIsDisplayed()
        composeTestRule.onNodeWithText("75 kg").assertIsDisplayed()
        composeTestRule.onNodeWithText("28 Aug 2024").assertIsDisplayed()
    }

    @Test
    fun whenDeleteButtonIsClicked_thenDeleteMovementConfirmationDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Name",
                weightUnit = WeightUnit.POUNDS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                )
            )
        }

        composeTestRule.onNodeWithText("Delete").performClick()

        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertIsDisplayed()
    }

    @Test
    fun givenDeleteMovementConfirmDialog_whenConfirmIsClicked_thenMenuIsDismissedAndMovementIsDeleted() {
        var deleteMovementCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Name",
                weightUnit = WeightUnit.POUNDS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                ),
                onDeleteMovementClick = { deleteMovementCalled = true }
            )
        }

        composeTestRule.onNodeWithText("Delete").performClick()
        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Yes, delete").performClick()

        composeTestRule.onNodeWithContentDescription("Delete movement confirmation dialog").assertDoesNotExist()
        assertTrue(deleteMovementCalled)
    }

    @Test
    fun whenAddResultButtonIsClicked_thenAddResultDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Name",
                weightUnit = WeightUnit.POUNDS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()

        composeTestRule.onNodeWithContentDescription("Add Result Dialog").assertIsDisplayed()
    }

    @Test
    fun givenAddResultDialogWithNoWeight_thenAddButtonIsDisabled() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Name",
                weightUnit = WeightUnit.POUNDS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()

        composeTestRule.onNodeWithText("Add result button").assertIsNotEnabled()
    }

    @Test
    fun givenAddResultDialogWithWeight_whenAddButtonIsClicked_thenDialogIsClosedAndResultIsAdded() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Name",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                ),
                addResult = { _, _ ->
                    addResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()
        composeTestRule.onNodeWithContentDescription("Weight Text Field").performTextInput("780")

        composeTestRule.onNodeWithText("Add result button").performClick()

        composeTestRule.onNodeWithContentDescription("Add Result Dialog").assertDoesNotExist()
        assertTrue(addResultCalled)
    }

    @Test
    fun givenAddResultDialogWithWeight_whenDismissButtonIsClicked_thenDialogIsClosedAndNoResultIsAdded() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Name",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                ),
                addResult = { _, _ ->
                    addResultCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add result button").performClick()
        composeTestRule.onNodeWithContentDescription("Weight Text Field").performTextInput("780")

        composeTestRule.onNodeWithText("Add result button").performClick()

        composeTestRule.onNodeWithContentDescription("Add Result Dialog").assertDoesNotExist()
        assertTrue(addResultCalled)
    }

    @Test
    fun whenResultIsClicked_thenEditResultDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Back Squat",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        listOf(
                            OneRMInfo(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC)
                            )
                        )
                    )
                )
            )
        }

        composeTestRule.onNodeWithText("70 kg").performClick()

        composeTestRule.onNodeWithContentDescription("Edit Result Dialog").assertIsDisplayed()
    }

    @Test
    fun givenEditDialog_whenDeleteIsClicked_thenDialogIsClosedAndResultIsDeleted() {
        var deleteCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Back Squat",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        listOf(
                            OneRMInfo(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC)
                            )
                        )
                    )
                ),
                onDeleteResultClick = {
                    deleteCalled = true
                }
            )
        }

        composeTestRule.onNodeWithText("70 kg").performClick()
        composeTestRule.onNodeWithContentDescription("Edit Result Dialog").assertIsDisplayed()

        composeTestRule.onNodeWithText("Delete result button").performClick()

        composeTestRule.onNodeWithContentDescription("Add Result Dialog").assertDoesNotExist()
        assertTrue(deleteCalled)
    }

    @Test
    fun givenEditDialog_whenDateIsClicked_thenCalendarDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Back Squat",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        listOf(
                            OneRMInfo(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC)
                            )
                        )
                    )
                )
            )
        }

        composeTestRule.onNodeWithText("70 kg").performClick()
        composeTestRule.onNodeWithContentDescription("Edit Result Dialog").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()
    }

    @Test
    fun givenCalendarDialog_whenDateIsSelectedAndAcceptButtonIsClicked_thenDialogIsClosedAndAddResultIsCalled() {
        var addResultCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementName = "Back Squat",
                weightUnit = WeightUnit.KILOGRAMS,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        listOf(
                            OneRMInfo(
                                id = 1,
                                movementId = 2,
                                weight = 70f,
                                offsetDateTime = OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC)
                            )
                        )
                    )
                ),
                addResult = { _, _ ->
                    addResultCalled = true
                },
            )
        }

        composeTestRule.onNodeWithText("70 kg").performClick()
        composeTestRule.onNodeWithContentDescription("Outlined Text Field Date Picker").performClick()
        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").isDisplayed()

        composeTestRule.onNodeWithText("Wednesday, July 17, 2024").performClick()
        composeTestRule.onNodeWithText("Accept").performClick()
        composeTestRule.onNodeWithContentDescription("Edit result button").performClick()

        composeTestRule.onNodeWithContentDescription("Date Picker Dialog").assertDoesNotExist()
        assertTrue(addResultCalled)
    }
}