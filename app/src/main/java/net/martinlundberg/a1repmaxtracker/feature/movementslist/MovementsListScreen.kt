package net.martinlundberg.a1repmaxtracker.feature.movementslist

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import net.martinlundberg.a1repmaxtracker.R
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.weightWithUnit
import net.martinlundberg.a1repmaxtracker.util.provideWeightUnitService

@Composable
fun MovementsListRoute(
    onMovementClick: (Movement) -> Unit = {},
    movementsListViewModel: MovementsListViewModel = hiltViewModel(),
    weightUnitService: WeightUnitService = provideWeightUnitService(),
) {
    LaunchedEffect(Unit) {
        movementsListViewModel.getMovements()
    }

    val movementsListUiState by movementsListViewModel.uiState.collectAsState()
    val weightUnit by weightUnitService.weightUnitFlow.collectAsState()

    MovementsListScreen(
        movementsListUiState = movementsListUiState,
        weightUnit = weightUnit,
        onAddMovementClick = movementsListViewModel::addMovement,
        onMovementClick = onMovementClick,
        onEditMovementClick = movementsListViewModel::editMovement,
        onDeleteMovementClick = movementsListViewModel::deleteMovement,
        setWeightUnitToPounds = weightUnitService::setWeightUnitToPounds
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementsListScreen(
    movementsListUiState: MovementsListUiState = Loading,
    weightUnit: String,
    onAddMovementClick: (Movement, String) -> Unit = { _, _ -> },
    onMovementClick: (Movement) -> Unit = {},
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (Long) -> Unit = {},
    setWeightUnitToPounds: (Boolean) -> Unit = {},
) {
    var movementToEdit by rememberSaveable { mutableStateOf<Movement?>(null) }
    var showAddMovementDialog by remember { mutableStateOf(false) }
    var movementToDelete by rememberSaveable { mutableStateOf<Movement?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(top = 24.dp),
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
                title = {
                    Text(
                        text = "1RM Tracker",
                        style = TextStyle(fontSize = 48.sp)
                    )
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = weightUnit, style = MaterialTheme.typography.titleLarge)
                        Box(modifier = Modifier.size(4.dp))
                        Switch(
                            checked = weightUnit == "lb",
                            onCheckedChange = {
                                setWeightUnitToPounds(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Your Top Scores", style = TextStyle(fontSize = 20.sp))

            when (movementsListUiState) {
                Loading -> {
                    Box(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }

                is Success -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        movementsListUiState.movements.map {
                            item {
                                MovementCard(
                                    movement = Movement(it.id, it.name, it.weight),
                                    weightUnit = weightUnit,
                                    onMovementClick = onMovementClick,
                                    onEditMovementClick = { movement ->
                                        movementToEdit = movement
                                    },
                                    onDeleteMovementClick = { movement ->
                                        movementToDelete = movement
                                    }
                                )
                            }
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .semantics { contentDescription = "Add Movement" },
                        onClick = { showAddMovementDialog = true },
                        shape = RoundedCornerShape(80.dp),
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 24.dp,
                                vertical = 12.dp
                            ),
                            text = "+ Add movement",
                            style = TextStyle(color = Color.White, fontSize = 18.sp)
                        )
                    }

                    if (showAddMovementDialog) {
                        AddMovementDialog(
                            weightUnit = weightUnit,
                            onDismissRequest = { showAddMovementDialog = false },
                            onConfirmation = { movement, weightUnit ->
                                onAddMovementClick(movement, weightUnit)
                                showAddMovementDialog = false
                            }
                        )
                    }

                    movementToEdit?.let { movement ->
                        EditMovementDialog(
                            movement = movement,
                            weightUnit = weightUnit,
                            onDismissRequest = { movementToEdit = null },
                            onConfirmation = { editedMovement, _ ->
                                onEditMovementClick(editedMovement)
                                movementToEdit = null
                            }
                        )
                    }

                    movementToDelete?.let { movement ->
                        DeleteMovementConfirmDialog(
                            name = movement.name,
                            onDismissRequest = { movementToDelete = null },
                            onConfirmation = {
                                onDeleteMovementClick(movement.id)
                                movementToDelete = null
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovementCard(
    movement: Movement,
    weightUnit: String,
    onMovementClick: (Movement) -> Unit,
    onEditMovementClick: (Movement) -> Unit,
    onDeleteMovementClick: (Movement) -> Unit,
) {
    var movementDropDownMenuInfo by rememberSaveable { mutableStateOf<Movement?>(null) }
    val view = LocalView.current
    Box {
        Card(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onMovementClick(movement) },
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
                    .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(movement.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
                if (movement.weight == null) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.titleLarge,
                    )
                } else {
                    Text(
                        text = movement.weight.weightWithUnit(weightUnit == "lb"),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                Box(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.movement_list_item_nav_icon),
                    contentDescription = "Navigation icon",
                )
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
    onDeleteMovementClick: (Movement) -> Unit = {},
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
                    onDeleteMovementClick(movement)
                }
            )
        }
    }
}

@Composable
fun DeleteMovementConfirmDialog(
    name: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    AlertDialog(
        modifier = Modifier.semantics { contentDescription = "Delete Movement Confirmation Dialog" },
        title = { Text("Delete Movement") },
        text = { Text("Are you sure you want to delete $name?") },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
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
    weightUnit: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Movement, String) -> Unit = { _, _ -> },
) {
    AddOrEditMovementDialog(
        isAdd = true,
        weightUnit = weightUnit,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation
    )
}

@Composable
fun EditMovementDialog(
    movement: Movement,
    weightUnit: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Movement, String) -> Unit = { _, _ -> },
) {
    AddOrEditMovementDialog(
        isAdd = false,
        movement = movement,
        weightUnit = weightUnit,
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation
    )
}

@Composable
private fun AddOrEditMovementDialog(
    isAdd: Boolean,
    movement: Movement = Movement(name = ""),
    weightUnit: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Movement, String) -> Unit = { _, _ -> },
) {
    var movementNameText by remember { mutableStateOf(movement.name) }
    val weightInitialValue = movement.weight?.toString() ?: ""
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
                if (isAdd) {
                    Spacer(modifier = Modifier.size(16.dp))
                    TextField(
                        value = movementWeightText,
                        onValueChange = { movementWeightText = it },
                        label = { Text("Weight ($weightUnit)") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = {
                            onConfirmation(
                                Movement(
                                    id = movement.id,
                                    name = movementNameText,
                                    weight = movementWeightText.toFloatOrNull()
                                ),
                                weightUnit,
                            )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementsListLoadingPreview() {
    _1RepMaxTrackerTheme {
        MovementsListScreen(
            movementsListUiState = Loading,
            weightUnit = "lb",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovementsListScreenSuccessPreview() {
    _1RepMaxTrackerTheme {
        MovementsListScreen(
            movementsListUiState = Success(
                listOf(
                    Movement(1, "Movement 1", 100f),
                    Movement(2, "Movement 4", 4.4f),
                    Movement(3, "No weight", null),
                )
            ),
            weightUnit = "kg"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMovementDialogPreview() {
    _1RepMaxTrackerTheme {
        AddOrEditMovementDialog(
            isAdd = true,
            weightUnit = "kg",
            movement = Movement(id = 1, name = "Movement 1", weight = 102.25f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMovementDialogPreview() {
    _1RepMaxTrackerTheme {
        AddOrEditMovementDialog(
            isAdd = false,
            weightUnit = "kg",
            movement = Movement(id = 1, name = "Movement 1", weight = 100.75f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteMovementConfirmDialogPreview() {
    _1RepMaxTrackerTheme {
        DeleteMovementConfirmDialog(name = "Movement 1")
    }
}