package net.martinlundberg.a1repmaxtracker.features.movementdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailUiState.Success

@Composable
fun MovementDetailRoute(movementDetailViewModel: MovementDetailViewModel = viewModel()) {
    movementDetailViewModel.getMovementInfo()
    val movementDetailUiState by movementDetailViewModel.uiState.collectAsState()
    MovementDetailScreen(movementDetailUiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDetailScreen(
    movementDetailUiState: MovementDetailUiState = Loading,
) {
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
        when (movementDetailUiState) {
            Loading -> {
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

            is Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = movementDetailUiState.movement.name)
                }
            }
        }
    }
}