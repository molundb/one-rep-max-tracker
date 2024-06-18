package net.martinlundberg.a1repmaxtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import net.martinlundberg.a1repmaxtracker.database.OneRepMaxTrackerDatabase
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            OneRepMaxTrackerDatabase::class.java, "database-name"
        ).build()

        setContent {
            _1RepMaxTrackerTheme {
                Navigation()
            }
        }
    }
}