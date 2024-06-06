package net.martinlundberg.a1repmaxtracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.martinlundberg.a1repmaxtracker.ui.HomeUiState
import net.martinlundberg.a1repmaxtracker.ui.Movement
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenListWithMovement_whenMainScreen_ThenMovementInfoIsDisplayed() {
        composeTestRule.setContent {
            _1RepMaxTrackerTheme {
                MainScreen(
                    homeUiState = HomeUiState.Success(
                        listOf(
                            Movement("Test movement", 2)
                        )
                    )
                )
            }
        }

        composeTestRule.onNodeWithText("Test movement").assertIsDisplayed()
        composeTestRule.onNodeWithText("2 kg").assertIsDisplayed()
    }

    @Test
    fun whenAddButtonIsPressed_ThenAddMovementDialogIsDisplayed() {
        composeTestRule.setContent {
            _1RepMaxTrackerTheme {
                MainScreen(
                    homeUiState = HomeUiState.Success(
                        listOf(
                            Movement("Test movement", 2)
                        )
                    )
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Add Movement").performClick()
        composeTestRule.onNodeWithText("Add Movement").assertIsDisplayed()
    }
}