package net.martinlundberg.onerepmaxtracker.feature.movementlist

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import net.martinlundberg.onerepmaxtracker.DefaultScaffold
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.analytics.LocalAnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.TrackScreenViewEvent
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovementConfirmDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovementConfirmDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_DeleteMovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_DeleteMovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_EditMovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_MovementLongClick
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.ui.components.ConfirmDeletionDialog
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.ui.theme.White
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.weightWithUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS

@Composable
fun MovementListRoute(
    innerPadding: PaddingValues,
    onMovementClick: (Movement, Lifecycle.State) -> Unit = { _, _ -> },
    movementListViewModel: MovementListViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        movementListViewModel.getMovements()
    }

    val movementListUiState by movementListViewModel.uiState.collectAsState()

    MovementListScreen(
        innerPadding = innerPadding,
        movementListUiState = movementListUiState,
        onAddMovementClick = movementListViewModel::addMovement,
        onMovementClick = onMovementClick,
        onEditMovementClick = movementListViewModel::editMovement,
        onDeleteMovementClick = movementListViewModel::deleteMovement,
        setAnalyticsCollectionEnabled = movementListViewModel::setAnalyticsCollectionEnabled,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovementListScreen(
    innerPadding: PaddingValues,
    movementListUiState: MovementListUiState = Loading,
    onAddMovementClick: (Movement, WeightUnit) -> Unit = { _, _ -> },
    onMovementClick: (Movement, Lifecycle.State) -> Unit = { _, _ -> },
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (Long) -> Unit = {},
    setAnalyticsCollectionEnabled: (Boolean) -> Unit = {},
) {
    TrackScreenViewEvent(screenName = "MovementList")

    var movementToEdit by remember { mutableStateOf<Movement?>(null) }
    var showAddMovementDialog by remember { mutableStateOf(false) }
    var movementToDelete by remember { mutableStateOf<Movement?>(null) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.movement_list_screen_list_title),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
        )

        when (movementListUiState) {
            Loading -> {
                Box(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .semantics {
                            contentDescription =
                                context.getString(R.string.loading_indicator_content_description)
                        },
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            is Success -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val analyticsHelper = LocalAnalyticsHelper.current
                if (movementListUiState.movements.isEmpty()) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Start by adding your first result",
                            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(top = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        movementListUiState.movements.map { movement ->
                            item(key = movement.id) {
                                MovementCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    movement = movement,
                                    weightUnit = movementListUiState.weightUnit,
                                    onMovementClick = onMovementClick,
                                    onEditMovementClick = { movement ->
                                        analyticsHelper.logMovementList_EditMovementClick(movement)
                                        movementToEdit = movement
                                    },
                                    onDeleteMovementClick = { movement ->
                                        analyticsHelper.logMovementList_DeleteMovementClick(movement)
                                        movementToDelete = movement
                                    }
                                )
                            }
                        }
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        modifier = Modifier
                            .semantics {
                                contentDescription =
                                    context.getString(R.string.movement_list_screen_add_movement_button_content_description)
                            },
                        onClick = {
                            analyticsHelper.logAddMovementClick()
                            showAddMovementDialog = true
                        },
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = stringResource(R.string.movement_list_screen_add_movement_button),
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Analytics",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Box(modifier = Modifier.size(4.dp))
                        Switch(
                            checked = movementListUiState.isAnalyticsEnabled,
                            onCheckedChange = {
                                setAnalyticsCollectionEnabled(it)
                            },
                        )
                    }
                }

                if (showAddMovementDialog) {
                    AddMovementDialog(
                        weightUnit = movementListUiState.weightUnit,
                        onDismissRequest = { showAddMovementDialog = false },
                        onCancel = { showAddMovementDialog = false },
                        onConfirm = { movement ->
                            onAddMovementClick(movement, movementListUiState.weightUnit)
                            showAddMovementDialog = false
                        }
                    )
                }

                movementToEdit?.let { movement ->
                    EditMovementDialog(
                        movement = movement,
                        weightUnit = movementListUiState.weightUnit,
                        onDismissRequest = { movementToEdit = null },
                        onCancel = { movementToEdit = null },
                        onConfirm = { editedMovement ->
                            onEditMovementClick(editedMovement)
                            movementToEdit = null
                        },
                        onDelete = { editedMovement ->
                            movementToDelete = editedMovement
                            movementToEdit = null
                        },
                    )
                }

                movementToDelete?.let { movement ->
                    DeleteMovementConfirmDialog(
                        movementId = movement.id,
                        movementName = movement.name,
                        onDismissRequest = {
                            movementToDelete = null
                        },
                        onCancel = {
                            movementToDelete = null
                        },
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
    modifier: Modifier = Modifier,
    movement: Movement,
    weightUnit: WeightUnit,
    onMovementClick: (Movement, Lifecycle.State) -> Unit,
    onEditMovementClick: (Movement) -> Unit,
    onDeleteMovementClick: (Movement) -> Unit,
) {
    var movementDropDownMenuInfo by remember { mutableStateOf<Movement?>(null) }
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val analyticsHelper = LocalAnalyticsHelper.current

    Card(
        modifier = modifier
            .combinedClickable(
                onClick = { onMovementClick(movement, lifecycleOwner.lifecycle.currentState) },
                onLongClick = {
                    analyticsHelper.logMovementList_MovementLongClick(movement)
                    view.performHapticFeedback(
                        HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                    )
                    movementDropDownMenuInfo = movement
                },
            )
            .semantics {
                contentDescription = context.getString(R.string.movement_list_screen_movement_card_content_description)
            },
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
                    text = stringResource(R.string.movement_list_screen_movement_card_no_weight),
                    style = MaterialTheme.typography.titleMedium,
                )
            } else {
                Text(
                    text = movement.weight.weightWithUnit(weightUnit, LocalContext.current),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Box(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_nav),
                contentDescription = stringResource(R.string.movement_list_screen_movement_card_nav_icon_content_description),
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
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
                .semantics {
                    contentDescription =
                        context.getString(R.string.movement_list_screen_movement_drop_down_menu_content_description)
                }
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {
                    onDismiss()
                }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    onClick = {
                        onDismiss()
                        onEditMovementClick(movement)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
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
    movementId: Long,
    movementName: String,
    onDismissRequest: () -> Unit = {},
    onCancel: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    ConfirmDeletionDialog(
        title = stringResource(R.string.delete_movement_confirm_dialog_title),
        movementName = movementName,
        cardContentDescription = stringResource(R.string.delete_movement_confirm_dialog_content_description),
        onDismissRequest = {
            analyticsHelper.logDeleteMovementConfirmDialog_Dismissed(movementId)
            onDismissRequest()
        },
        onCancel = {
            analyticsHelper.logDeleteMovementConfirmDialog_CancelClick(movementId)
            onCancel()
        },
        onConfirmation = onConfirmation,
    )
}

@Composable
fun AddMovementDialog(
    weightUnit: WeightUnit,
    onDismissRequest: (Movement) -> Unit = {},
    onCancel: (Movement) -> Unit = {},
    onConfirm: (Movement) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    AddOrEditMovementDialog(
        movement = Movement(name = ""),
        weightUnit = weightUnit,
        cardContentDescription = stringResource(R.string.movement_list_screen_add_movement_dialog_content_description),
        title = stringResource(R.string.movement_list_screen_add_movement_dialog_title),
        showWeightField = true,
        confirmButtonText = stringResource(R.string.movement_list_screen_add_movement_dialog_confirm_button),
        onDismissRequest = { movement ->
            analyticsHelper.logAddMovementDialog_Dismissed(movement)
            onDismissRequest(movement)
        },
        onCancel = { movement ->
            analyticsHelper.logAddMovementDialog_CancelClick(movement)
            onCancel(movement)
        },
        onConfirm = { movement ->
            analyticsHelper.logAddMovementDialog_ConfirmClick(movement)
            onConfirm(movement)
        },
    )
}

@Composable
fun EditMovementDialog(
    movement: Movement,
    weightUnit: WeightUnit,
    onDismissRequest: (Movement) -> Unit = {},
    onCancel: (Movement) -> Unit = {},
    onConfirm: (Movement) -> Unit = { },
    onDelete: (Movement) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    AddOrEditMovementDialog(
        movement = movement,
        weightUnit = weightUnit,
        cardContentDescription = stringResource(R.string.movement_list_screen_edit_movement_dialog_content_description),
        title = stringResource(R.string.movement_list_screen_edit_movement_dialog_title),
        showWeightField = false,
        confirmButtonText = stringResource(R.string.save),
        onDismissRequest = { editedMovement ->
            analyticsHelper.logEditMovementDialog_Dismissed(editedMovement)
            onDismissRequest(editedMovement)
        },
        onCancel = { editedMovement ->
            analyticsHelper.logEditMovementDialog_CancelClick(editedMovement)
            onCancel(editedMovement)
        },
        onConfirm = { editedMovement ->
            analyticsHelper.logEditMovementDialog_ConfirmClick(editedMovement)
            onConfirm(editedMovement)
        },
        onDelete = { editedMovement ->
            analyticsHelper.logEditMovementDialog_DeleteMovementClick(editedMovement)
            onDelete(editedMovement)
        },
    )
}

@Composable
private fun AddOrEditMovementDialog(
    movement: Movement,
    weightUnit: WeightUnit,
    cardContentDescription: String,
    title: String,
    showWeightField: Boolean,
    confirmButtonText: String,
    onDismissRequest: (Movement) -> Unit = {},
    onCancel: (Movement) -> Unit = {},
    onConfirm: (Movement) -> Unit = { },
    onDelete: ((Movement) -> Unit)? = null,
) {
    var movementNameText by remember { mutableStateOf(TextFieldValue(movement.name, TextRange(movement.name.length))) }
    val weightInitialValue = movement.weight?.toString() ?: ""
    var movementWeightText by remember { mutableStateOf(weightInitialValue) }

    Dialog(onDismissRequest = {
        onDismissRequest(
            createMovementOfInput(
                movementId = movement.id,
                movementNameText = movementNameText.text,
                movementWeightText = movementWeightText,
            ),
        )
    }) {
        val focusRequester = remember { FocusRequester() }
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = cardContentDescription
                },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge
                )
                Box(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.movement_list_screen_add_or_edit_movement_dialog_name_label),
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
                        .semantics {
                            contentDescription =
                                context.getString(R.string.movement_list_screen_add_or_edit_movement_dialog_name_field_content_description)
                        },
                )
                if (showWeightField) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = stringResource(
                            R.string.movement_list_screen_add_or_edit_movement_dialog_weight_label,
                            weightUnit.toString(LocalContext.current)
                        ),
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
                        onClick = {
                            onCancel(
                                createMovementOfInput(
                                    movementId = movement.id,
                                    movementNameText = movementNameText.text,
                                    movementWeightText = movementWeightText,
                                ),
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Black,
                        ),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Box(modifier = Modifier.width(32.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = {
                            onConfirm(
                                createMovementOfInput(
                                    movementId = movement.id,
                                    movementNameText = movementNameText.text,
                                    movementWeightText = movementWeightText,
                                ),
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
                        Text(confirmButtonText)
                    }
                }
                Box(modifier = Modifier.height(24.dp))
                if (onDelete != null) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
                            val editedMovement = createMovementOfInput(
                                movementId = movement.id,
                                movementNameText = movementNameText.text,
                                movementWeightText = movementWeightText,
                            )
                            onDelete(editedMovement)
                        },
                    ) {
                        Text(text = stringResource(R.string.movement_list_screen_edit_movement_dialog_delete_button))
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

private fun createMovementOfInput(
    movementId: Long,
    movementNameText: String,
    movementWeightText: String,
) = Movement(
    id = movementId,
    name = movementNameText,
    weight = movementWeightText.toFloatOrNull()
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementListLoadingPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            MovementListScreen(
                innerPadding = innerPadding,
                movementListUiState = Loading,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementListScreenSuccessEmptyPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            MovementListScreen(
                innerPadding = innerPadding,
                movementListUiState = Success(
                    listOf(),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementListScreenSuccessContentPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            MovementListScreen(
                innerPadding = innerPadding,
                movementListUiState = Success(
                    listOf(
                        Movement(1, "Movement 1", 100f),
                        Movement(2, "Movement 4", 4.4f),
                        Movement(3, "No weight", null),
                    ),
                    weightUnit = KILOGRAMS,
                    isAnalyticsEnabled = false,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddMovementDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        AddMovementDialog(
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMovementDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        EditMovementDialog(
            movement = Movement(id = 1, name = "Movement 1", weight = 100.75f),
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMovementDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        EditMovementDialog(
            movement = Movement(id = 1, name = "", weight = 55.75f),
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteMovementConfirmDialogPreview() {
    OneRepMaxTrackerTheme {
        DeleteMovementConfirmDialog(
            movementId = 5,
            movementName = "Movement 1",
        )
    }
}