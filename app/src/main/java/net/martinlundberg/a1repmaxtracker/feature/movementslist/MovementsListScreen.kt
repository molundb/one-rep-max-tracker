package net.martinlundberg.a1repmaxtracker.feature.movementslist

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success
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
        onAddMovementClick = movementsListViewModel::addMovement,
        onMovementClick = onMovementClick,
        onDeleteMovementClick = movementsListViewModel::deleteMovement
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsListScreen(
    movementsListUiState: MovementsListUiState = Loading,
    onAddMovementClick: (Movement) -> Unit = {},
    onMovementClick: (String) -> Unit = {},
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (String) -> Unit = {},
) {
    var movementToEdit by rememberSaveable { mutableStateOf<Movement?>(null) }
    var movementToDelete by rememberSaveable { mutableStateOf<String?>(null) }

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
                                movement = Movement(it.id, it.name, it.weight),
                                onMovementClick = onMovementClick,
                                onEditMovementClick = { name ->
                                    movementToEdit = name
                                },
                                onDeleteMovementClick = { name ->
                                    movementToDelete = name
                                }
                            )
                        }
                    }
                }
                FloatingActionButton(
                    modifier = Modifier
                        .size(80.dp)
                        .semantics { contentDescription = "Add Movement" },
                    onClick = { movementToEdit = Movement(-1, "") },
                ) {
                    Icon(Filled.Add, "Floating action button.")
                }

                movementToEdit?.let { movement ->
                    if (movement.name.isEmpty()) {
                        AddMovementDialog(
                            movement = movement,
                            onDismissRequest = { movementToEdit = null },
                            onConfirmation = {
                                onAddMovementClick(it)
                                movementToEdit = null
                            }
                        )
                    } else {
                        EditMovementDialog(
                            movement = movement,
                            onDismissRequest = { movementToEdit = null },
                            onConfirmation = {
                                onEditMovementClick(it)
                                movementToEdit = null
                            }
                        )
                    }
                }

                movementToDelete?.let { name ->
                    DeleteMovementConfirmDialog(
                        name = name,
                        onDismissRequest = { movementToDelete = null },
                        onConfirmation = {
                            onDeleteMovementClick(it)
                            movementToDelete = null
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
    movement: Movement,
    onMovementClick: (String) -> Unit,
    onEditMovementClick: (Movement) -> Unit,
    onDeleteMovementClick: (String) -> Unit,
) {
    var movementDropDownMenuInfo by rememberSaveable { mutableStateOf<Movement?>(null) }
    val view = LocalView.current
    Box {
        Card(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onMovementClick(movement.name) },
                    onLongClick = {
                        view.performHapticFeedback(
                            HapticFeedbackConstants.LONG_PRESS,
                            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                        )
                        movementDropDownMenuInfo = movement
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
                Text(movement.name, style = MaterialTheme.typography.titleLarge)
                if (movement.weight == null) {
                    Text(
                        modifier = Modifier.padding(end = 12.dp),
                        text = "-", style = MaterialTheme.typography.titleLarge
                    )
                } else {
                    Text("${movement.weight} kg", style = MaterialTheme.typography.titleLarge)
                }
            }
            movementDropDownMenuInfo?.let {
                MovementDropDownMenu(
                    movement = it,
                    onEditMovementClick = onEditMovementClick,
                    onDeleteMovementClick = onDeleteMovementClick,
                    onDismiss = { movementDropDownMenuInfo = null }
                )
            }
        }
    }
}

@Composable
fun MovementDropDownMenu(
    movement: Movement,
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (String) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
            .semantics { contentDescription = "Movement Drop Down Menu" }
    ) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = {
                onDismiss()
            }
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    onDismiss()
                    onEditMovementClick(movement)
                }
            )
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    onDismiss()
                    onDeleteMovementClick(movement.name)
                }
            )
        }
    }
}

@Composable
fun DeleteMovementConfirmDialog(
    name: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (String) -> Unit = {},
) {
    AlertDialog(
        modifier = Modifier.semantics { contentDescription = "Delete Movement Confirmation Dialog" },
        title = { Text("Delete Movement") },
        text = { Text("Are you sure you want to delete $name?") },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(name)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun AddMovementDialog(
    movement: Movement,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Movement) -> Unit = {},
) {
    AddOrEditMovementDialog(
        isAdd = true,
        movement = movement,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation
    )
}

@Composable
fun EditMovementDialog(
    movement: Movement,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Movement) -> Unit = {},
) {
    AddOrEditMovementDialog(
        isAdd = false,
        movement = movement,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation
    )
}

@Composable
private fun AddOrEditMovementDialog(
    isAdd: Boolean = true,
    movement: Movement,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Movement) -> Unit = {},
) {
    var movementNameText by remember { mutableStateOf(movement.name) }
    val weightInitialValue = if (movement.weight == null) "" else movement.weight.toString()
    var movementWeightText by remember { mutableStateOf(weightInitialValue) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        val focusRequester = remember { FocusRequester() }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = if (isAdd) "Add Movement Dialog" else "Edit Movement Dialog" },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            ) {
                TextField(
                    value = movementNameText,
                    onValueChange = { movementNameText = it },
                    label = { Text("Name of exercise") },
                    modifier = Modifier.focusRequester(focusRequester),
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(
                    value = movementWeightText,
                    onValueChange = { movementWeightText = it },
                    label = { Text("Weight (kg)") },
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
                    TextButton(
                        onClick = {
                            onConfirmation(Movement(1, movementNameText, movementWeightText.toIntOrNull()))
                        },
                        enabled = movementNameText.isNotBlank()
                    ) {
                        Text(if (isAdd) "Add" else "Edit")
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
                    Movement(1, "Movement 1", 100),
                    Movement(2, "Movement 4", 4),
                    Movement(3, "No weight", null),
                )
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddMovementDialogPreview() {
    _1RepMaxTrackerTheme {
        AddOrEditMovementDialog(movement = Movement(id = 1, name = "Movement 1", weight = 100))
    }
}

@Preview(showBackground = true)
@Composable
fun DeleteMovementConfirmDialogPreview() {
    _1RepMaxTrackerTheme {
        DeleteMovementConfirmDialog(name = "Movement 1")
    }
}