package net.martinlundberg.onerepmaxtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledServiceImpl
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.LocalAnalyticsHelper
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.ui.theme.Red
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var analyticsService: AnalyticsEnabledServiceImpl

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Red.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        setContent {
            CompositionLocalProvider(
                LocalAnalyticsHelper provides analyticsHelper
            ) {
                OneRepMaxTrackerTheme {
                    Navigation()
                }
            }
        }
    }
}