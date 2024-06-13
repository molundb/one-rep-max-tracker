package net.martinlundberg.a1repmaxtracker.features.movementslist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
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
fun MovementsListRoute(
    onMovementClick: (String) -> Unit = {},
    movementsListViewModel: MovementsListViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        movementsListViewModel.getMovements()

    }
    val movementsListUiState by movementsListViewModel.uiState.collectAsState()
    MovementsListScreen(
        movementsListUiState = movementsListUiState,
        addMovement = movementsListViewModel::addMovement,
        onMovementClick = onMovementClick,
        onDeleteMovementClick = movementsListViewModel::deleteMovement
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsListScreen(
    movementsListUiState: MovementsListUiState = Loading,
    addMovement: (String) -> Unit = {},
    onMovementClick: (String) -> Unit = {},
    onDeleteMovementClick: (String) -> Unit = {},
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
                            MovementCard(
                                name = it.name,
                                weight = it.weight,
                                onMovementClick = onMovementClick,
                                onDeleteMovementClick = onDeleteMovementClick
                            )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovementCard(
    name: String,
    weight: Int?,
    onMovementClick: (String) -> Unit,
    onDeleteMovementClick: (String) -> Unit,
) {
    var movementName by rememberSaveable { mutableStateOf<String?>(null) }
    val haptics = LocalHapticFeedback.current
    Box {
        Card(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onMovementClick(name) },
                    onLongClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        movementName = name
                    },
                )
                .semantics { contentDescription = "Movement Card" },
        ) {
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
            movementName?.let {
                MovementDropDownMenu(
                    name = it,
                    onDeleteMovementClick = onDeleteMovementClick
                )
            }
        }
    }
}

@Composable
fun MovementDropDownMenu(
    name: String,
    onDeleteMovementClick: (String) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .semantics { contentDescription = "Movement Drop Down Menu" }
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = { }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    expanded = false
                    onDeleteMovementClick(name)
                }
            )
        }
    }
}

@Composable
fun AddMovementDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        val focusRequester = remember { FocusRequester() }
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
                    label = { Text("Name of exercise") },
                    modifier = Modifier.focusRequester(focusRequester),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation(text) },
                        enabled = text.isNotBlank()
                    ) {
                        Text("Add")
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovementsListLoadingPreview() {
    _1RepMaxTrackerTheme {
        MovementsListScreen(
            movementsListUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovementsListScreenContentPreview() {
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