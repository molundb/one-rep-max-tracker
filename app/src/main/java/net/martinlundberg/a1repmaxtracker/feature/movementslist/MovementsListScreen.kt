package net.martinlundberg.a1repmaxtracker.feature.movementslist

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import net.martinlundberg.a1repmaxtracker.R
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementslist.MovementsListUiState.Success
import net.martinlundberg.a1repmaxtracker.ui.theme.Black
import net.martinlundberg.a1repmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.a1repmaxtracker.ui.theme.White
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.weightWithUnit
import net.martinlundberg.a1repmaxtracker.util.provideWeightUnitService

@Composable
fun MovementsListRoute(
    innerPadding: PaddingValues,
    onMovementClick: (Movement, Lifecycle.State) -> Unit = { _, _ -> },
    movementsListViewModel: MovementsListViewModel = hiltViewModel(),
    weightUnitService: WeightUnitService = provideWeightUnitService(),
) {
    LaunchedEffect(Unit) {
        movementsListViewModel.getMovements()
    }

    val movementsListUiState by movementsListViewModel.uiState.collectAsState()
    val weightUnit by weightUnitService.weightUnitFlow.collectAsState()

    MovementsListScreen(
        innerPadding = innerPadding,
        movementsListUiState = movementsListUiState,
        weightUnit = weightUnit,
        onAddMovementClick = movementsListViewModel::addMovement,
        onMovementClick = onMovementClick,
        onEditMovementClick = movementsListViewModel::editMovement,
        onDeleteMovementClick = movementsListViewModel::deleteMovement,
    )
}

@Composable
fun MovementsListScreen(
    innerPadding: PaddingValues,
    movementsListUiState: MovementsListUiState = Loading,
    weightUnit: String,
    onAddMovementClick: (Movement, String) -> Unit = { _, _ -> },
    onMovementClick: (Movement, Lifecycle.State) -> Unit = { _, _ -> },
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (Long) -> Unit = {},
) {
    var movementToEdit by remember { mutableStateOf<Movement?>(null) }
    var showAddMovementDialog by remember { mutableStateOf(false) }
    var movementToDelete by remember { mutableStateOf<Movement?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Your top results", style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp))

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
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
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
                        movementName = movement.name,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovementCard(
    movement: Movement,
    weightUnit: String,
    onMovementClick: (Movement, Lifecycle.State) -> Unit,
    onEditMovementClick: (Movement) -> Unit,
    onDeleteMovementClick: (Movement) -> Unit,
) {
    var movementDropDownMenuInfo by remember { mutableStateOf<Movement?>(null) }
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = { onMovementClick(movement, lifecycleOwner.lifecycle.currentState) },
                onLongClick = {
                    view.performHapticFeedback(
                        HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    )
                    movementDropDownMenuInfo = movement
                },
            )
            .semantics { contentDescription = "Movement Card" },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp) // Minimum recommended height for clickable cards
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(movement.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            if (movement.weight == null) {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.titleMedium,
                )
            } else {
                Text(
                    text = movement.weight.weightWithUnit(weightUnit == "lb"),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Box(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_nav),
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

@Composable
fun MovementDropDownMenu(
    movement: Movement,
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (Movement) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = White,
            onSurface = Black,
        )
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
}

@Composable
fun DeleteMovementConfirmDialog(
    movementName: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp)
                .semantics { contentDescription = "Delete Movement Confirmation Dialog" },
            shape = RoundedCornerShape(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = "Delete movement",
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Are you sure you want to delete:",
                    style = MaterialTheme.typography.titleMedium,
                )
                Box(modifier = Modifier.height(12.dp))
                Text(
                    text = movementName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Black,
                        ),
                    ) {
                        Text("Cancel")
                    }
                    Box(modifier = Modifier.width(32.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onConfirmation() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) {
                        Text("Yes, delete")
                    }
                }
            }
        }
    }
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
    var movementNameText by remember { mutableStateOf(TextFieldValue(movement.name, TextRange(movement.name.length))) }
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
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = if (isAdd) "Add movement" else "Edit Movement",
                    style = MaterialTheme.typography.headlineLarge
                )
                Box(modifier = Modifier.height(24.dp))
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.titleMedium
                )
                Box(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = movementNameText,
                    onValueChange = { movementNameText = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .semantics { contentDescription = "Movement Name Text Field" },
                )
                if (isAdd) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = "Weight ($weightUnit)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Box(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = movementWeightText,
                        onValueChange = { movementWeightText = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Box(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = { onDismissRequest() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Black,
                        ),
                    ) {
                        Text("Cancel")
                    }
                    Box(modifier = Modifier.width(32.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = {
                            onConfirmation(
                                Movement(
                                    id = movement.id,
                                    name = movementNameText.text,
                                    weight = movementWeightText.toFloatOrNull()
                                ),
                                weightUnit,
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        ),
                        enabled = movementNameText.text.isNotBlank()
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
    OneRepMaxTrackerTheme {
        MovementsListScreen(
            innerPadding = PaddingValues(24.dp),
            movementsListUiState = Loading,
            weightUnit = "lb",
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementsListScreenSuccessPreview() {
    OneRepMaxTrackerTheme {
        MovementsListScreen(
            innerPadding = PaddingValues(24.dp),
            movementsListUiState = Success(
                listOf(
                    Movement(1, "Movement 1", 100f),
                    Movement(2, "Movement 4", 4.4f),
                    Movement(3, "No weight", null),
                )
            ),
            weightUnit = "kg",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMovementDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditMovementDialog(
            isAdd = true,
            weightUnit = "kg",
            movement = Movement(id = 1, name = "Movement 1", weight = 102.25f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMovementDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditMovementDialog(
            isAdd = true,
            weightUnit = "kg",
            movement = Movement(id = 1, name = "", weight = 102.25f)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMovementDialogPreview() {
    OneRepMaxTrackerTheme {
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
    OneRepMaxTrackerTheme {
        DeleteMovementConfirmDialog(movementName = "Movement 1")
    }
}