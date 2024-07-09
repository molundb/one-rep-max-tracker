package net.martinlundberg.a1repmaxtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import net.martinlundberg.a1repmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.a1repmaxtracker.ui.theme.Red

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Red.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
        )

        setContent {
            OneRepMaxTrackerTheme {
                Navigation()
            }
        }
    }
}