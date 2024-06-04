package net.martinlundberg.a1repmaxtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.martinlundberg.a1repmaxtracker.ui.HomeUiState
import net.martinlundberg.a1repmaxtracker.ui.HomeViewModel
import net.martinlundberg.a1repmaxtracker.ui.Movement
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: HomeViewModel by viewModels()
        viewModel.getMovements()

        setContent {
            _1RepMaxTrackerTheme {
                MainRoute(viewModel)
            }
        }
    }
}

@Composable
fun MainRoute(homeViewModel: HomeViewModel) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    MainScreen(homeUiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(homeUiState: HomeUiState = HomeUiState.Loading) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "1RM Tracker") },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        when (homeUiState) {
            HomeUiState.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Loading...")
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is HomeUiState.Success -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    homeUiState.movements.map {
                        item {
                            MovementCard(it.name, it.weight)
                        }
                    }
                }
                FloatingActionButton(
                    modifier = Modifier.size(80.dp),
                    onClick = { onClick() },
                ) {
                    Icon(Filled.Add, "Floating action button.")
                }
            }
        }
    }
}

@Composable
fun MovementCard(name: String, weight: Int) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.titleLarge)
            Text("$weight kg", style = MaterialTheme.typography.titleLarge)
        }
    }
}

fun onClick() {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun MainScreenLoadingPreview() {
    _1RepMaxTrackerTheme {
        MainScreen(HomeUiState.Loading)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenContentPreview() {
    _1RepMaxTrackerTheme {
        MainScreen(
            HomeUiState.Success(
                listOf(
                    Movement("Movement 1", 100),
                    Movement("Movement 4", 4),
                )
            )
        )
    }
}