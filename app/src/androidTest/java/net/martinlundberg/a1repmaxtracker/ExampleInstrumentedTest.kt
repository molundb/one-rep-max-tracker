package net.martinlundberg.a1repmaxtracker

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class ExampleInstrumentedTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Test
    fun givenA_whenB_ThenC() {
        composeTestRule.setContent {
            _1RepMaxTrackerTheme {
                MainScreen()
            }
        }

        composeTestRule.onNodeWithText("Welcome").assertIsDisplayed()
    }
}