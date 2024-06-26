package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
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
                movementName = "Name of movement",
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                )
            )
        }

        composeTestRule.onNodeWithText("Name of movement").assertIsDisplayed()
    }

    @Test
    fun givenStateIsLoading_thenLoadingIndicatorAndTextAreDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Loading
            )
        }

        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Circular Progress Indicator").assertIsDisplayed()
    }

    @Test
    fun givenListOf1RMs_thenListInfoIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail(
                        listOf(
                            OneRMInfo(70, OffsetDateTime.of(2024, 6, 10, 0, 0, 0, 0, ZoneOffset.UTC)),
                            OneRMInfo(72, OffsetDateTime.of(2024, 7, 17, 0, 0, 0, 0, ZoneOffset.UTC)),
                            OneRMInfo(75, OffsetDateTime.of(2024, 8, 28, 0, 0, 0, 0, ZoneOffset.UTC)),
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
    fun givenAdd1RMButtonIsPressed_thenAdd1RMDialogIsDisplayed() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add 1RM").performClick()

        composeTestRule.onNodeWithContentDescription("Add 1RM Dialog").assertIsDisplayed()
    }

    @Test
    fun givenAdd1RMDialogWithNoWeight_thenAddButtonIsDisabled() {
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                )
            )
        }

        composeTestRule.onNodeWithContentDescription("Add 1RM").performClick()

        composeTestRule.onNodeWithText("Add").assertIsNotEnabled()
    }

    @Test
    fun givenAdd1RMDialogWithWeight_whenAddButtonIsPressed_thenDialogIsClosedAndNew1RMIsAdded() {
        var add1RMCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                ),
                add1RM = { _, _ ->
                    add1RMCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add 1RM").performClick()
        composeTestRule.onNodeWithText("Weight").performTextInput("780")

        composeTestRule.onNodeWithText("Add").performClick()

        composeTestRule.onNodeWithContentDescription("Add 1RM Dialog").assertDoesNotExist()
        assertTrue(add1RMCalled)
    }

    @Test
    fun givenAdd1RMDialogWithWeight_whenDismissButtonIsPressed_thenDialogIsClosedAndNoNew1RMIsAdded() {
        var add1RMCalled = false
        composeTestRule.setContent {
            MovementDetailScreen(
                movementDetailUiState = MovementDetailUiState.Success(
                    MovementDetail()
                ),
                add1RM = { _, _ ->
                    add1RMCalled = true
                }
            )
        }

        composeTestRule.onNodeWithContentDescription("Add 1RM").performClick()
        composeTestRule.onNodeWithText("Weight").performTextInput("780")

        composeTestRule.onNodeWithText("Add").performClick()

        composeTestRule.onNodeWithContentDescription("Add 1RM Dialog").assertDoesNotExist()
        assertTrue(add1RMCalled)
    }
}