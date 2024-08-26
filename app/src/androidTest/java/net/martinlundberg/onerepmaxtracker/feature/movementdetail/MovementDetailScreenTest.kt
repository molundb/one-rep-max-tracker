package net.martinlundberg.onerepmaxtracker.feature.movementdetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertTrue
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.OffsetDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class MovementDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun givenLoading_thenLoadingIndicatorAndMovementNameAreDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 15,
                movementDetailUiState = MovementDetailUiState.Loading(MovementDetail("Movement name")),
            )
        }
        composeTestRule.onNodeWithText("Movement name").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular progress indicator").assertIsDisplayed()
    }

    @Test
    fun givenListWithResult_thenResultInfoIsDisplayed() {
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
                    weightUnit = KILOGRAMS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
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
    fun whenNavBackButtonIsClicked_thenNavigateBack() {
        var navigateBackCalled = false

        composeTestRule.setContent {
            MovementDetailScreen(
                innerPadding = PaddingValues(),
                movementId = 17,
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail("Name"),
                    weightUnit = WeightUnit.POUNDS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
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
    fun whenResultIsClicked_thenOnResultClickCalled() {
        // Given
        var onResultClickCalled = false

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
                        ),
                    ),
                    weightUnit = KILOGRAMS,
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                ),
                onResultClick = { _, _, _ ->
                    onResultClickCalled = true
                }
            )
        }

        // When
        composeTestRule.onNodeWithText("70 kg").performClick()

        // Then
        assertTrue(onResultClickCalled)
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
                    OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC),
                )
            )
        }

        composeTestRule.onNodeWithText("Edit").performClick()

        composeTestRule.onNodeWithContentDescription("Edit movement dialog").assertIsDisplayed()
    }

    @Test
    fun whenAddButtonIsClicked_thenAddResultDialogIsDisplayed() {
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

        composeTestRule.onNodeWithText("+ Add new").performClick()

        composeTestRule.onNodeWithContentDescription("Add result dialog").assertIsDisplayed()
    }
}