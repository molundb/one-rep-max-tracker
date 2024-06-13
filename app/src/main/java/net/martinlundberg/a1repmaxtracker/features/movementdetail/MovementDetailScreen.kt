package net.martinlundberg.a1repmaxtracker.features.movementdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.features.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme

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
    var showAdd1rmDialog by remember { mutableStateOf(false) }

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
                    FloatingActionButton(
                        modifier = Modifier
                            .size(80.dp)
                            .semantics { contentDescription = "Add Movement" },
                        onClick = { showAdd1rmDialog = true },
                    ) {
                        Icon(Filled.Add, "Floating action button.")
                    }
                    if (showAdd1rmDialog) {
                        Add1rmDialog(
                            onDismissRequest = { showAdd1rmDialog = false },
                            onConfirmation = {
//                                addMovement(it)
                                showAdd1rmDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Add1rmDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Add 1RM Dialog" },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Name of exercise") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
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
fun MovementDetailLoadingPreview() {
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementDetailUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovementDetailScreenContentPreview() {
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementDetailUiState = Success(
                MovementDetail("Movement Name")
            )
        )
    }
}