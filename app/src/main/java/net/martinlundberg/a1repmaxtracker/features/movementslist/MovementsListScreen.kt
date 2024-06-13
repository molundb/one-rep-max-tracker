package net.martinlundberg.a1repmaxtracker.features.movementslist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import net.martinlundberg.a1repmaxtracker.features.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.features.movementslist.MovementsListUiState.Success
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme

@Composable
fun MovementsListRoute(movementsListViewModel: MovementsListViewModel = viewModel()) {
    movementsListViewModel.getMovements()
    val movementsListUiState by movementsListViewModel.uiState.collectAsState()
    MovementsListScreen(movementsListUiState, movementsListViewModel::addMovement)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsListScreen(
    movementsListUiState: MovementsListUiState = MovementsListUiState.Loading,
    addMovement: (String) -> Unit = {},
) {
    var showAddMovementDialog by remember { mutableStateOf(false) }

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
        when (movementsListUiState) {
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

            is Success -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    movementsListUiState.movements.map {
                        item {
                            MovementCard(it.name, it.weight)
                        }
                    }
                }
                FloatingActionButton(
                    modifier = Modifier
                        .size(80.dp)
                        .semantics { contentDescription = "Add Movement" },
                    onClick = { showAddMovementDialog = true },
                ) {
                    Icon(Filled.Add, "Floating action button.")
                }

                if (showAddMovementDialog) {
                    AddMovementDialog(
                        onDismissRequest = { showAddMovementDialog = false },
                        onConfirmation = {
                            addMovement(it)
                            showAddMovementDialog = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementCard(name: String, weight: Int?) {
    Card(modifier = Modifier.semantics { contentDescription = "Movement Card" }, onClick = { navigateToMovement() }) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.titleLarge)
            if (weight == null) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = "-", style = MaterialTheme.typography.titleLarge
                )
            } else {
                Text("$weight kg", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

fun navigateToMovement() {
    TODO("Not yet implemented")
}

@Composable
fun AddMovementDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Add Movement Dialog" },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Name of exercise") }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Dismiss")
                    }
                    TextButton(onClick = { onConfirmation(text) }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenLoadingPreview() {
    _1RepMaxTrackerTheme {
        MovementsListScreen(
            movementsListUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenContentPreview() {
    _1RepMaxTrackerTheme {
        MovementsListScreen(
            movementsListUiState = Success(
                listOf(
                    Movement("Movement 1", 100),
                    Movement("Movement 4", 4),
                    Movement("No weight", null),
                )
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddMovementDialogPreview() {
    _1RepMaxTrackerTheme {
        AddMovementDialog(
            onDismissRequest = {},
            onConfirmation = {},
        )
    }
}