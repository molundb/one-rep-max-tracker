package net.martinlundberg.a1repmaxtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.room.Room
import net.martinlundberg.a1repmaxtracker.data.database.OneRepMaxTrackerDatabase
import net.martinlundberg.a1repmaxtracker.data.repository.DefaultMovementsRepository
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListViewModel
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListViewModelFactory
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            OneRepMaxTrackerDatabase::class.java, "database-name"
        ).build()

        val movementsRepository = DefaultMovementsRepository(db.movementDao())
        val viewModel: MovementsListViewModel by viewModels {
            MovementsListViewModelFactory(movementsRepository) // Pass your repository instance here
        }

        setContent {
            _1RepMaxTrackerTheme {
                Navigation(viewModel)
            }
        }
    }
}