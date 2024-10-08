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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import net.martinlundberg.onerepmaxtracker.DefaultScaffold
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.analytics.LocalAnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.TrackScreenViewEvent
import net.martinlundberg.onerepmaxtracker.analytics.logMovementListDropDownMenu_DeleteClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementListDropDownMenu_EditClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_AddMovementButtonClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_MovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementList_MovementLongClick
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementlist.MovementListUiState.Success
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.AddMovementDialog
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.DeleteMovementConfirmDialog
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.EditMovementDialog
import net.martinlundberg.onerepmaxtracker.ui.components.menus.MovementDropDownMenu
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.Companion.multiplyIfPoundsAndRoundToNearestQuarter
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS

@Composable
fun MovementListRoute(
    innerPadding: PaddingValues,
    onMovementClick: (Movement, Lifecycle.State) -> Unit = { _, _ -> },
    movementListViewModel: MovementListViewModel = hiltViewModel(),
) {
    val movementListUiState by movementListViewModel.uiState.collectAsState()

    MovementListScreen(
        innerPadding = innerPadding,
        movementListUiState = movementListUiState,
        onAddMovementClick = movementListViewModel::addMovement,
        onMovementClick = onMovementClick,
        onEditMovementClick = movementListViewModel::editMovement,
        onDeleteMovementClick = movementListViewModel::deleteMovement,
        setAnalyticsCollectionEnabled = movementListViewModel::setAnalyticsCollectionEnabled,
        onLatestOrBestResultsSwitchChecked = movementListViewModel::showLatestOrBestResults,
    )
}

@Composable
fun MovementListScreen(
    innerPadding: PaddingValues,
    movementListUiState: MovementListUiState = Loading,
    onAddMovementClick: (Movement, WeightUnit) -> Unit = { _, _ -> },
    onMovementClick: (Movement, Lifecycle.State) -> Unit = { _, _ -> },
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (Long) -> Unit = {},
    setAnalyticsCollectionEnabled: (Boolean) -> Unit = {},
    onLatestOrBestResultsSwitchChecked: (Boolean) -> Unit = {},
) {
    TrackScreenViewEvent(screenName = "MovementList")

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
            Loading -> LoadingUi()
            is Success -> SuccessUi(
                movementListUiState,
                onMovementClick,
                setAnalyticsCollectionEnabled,
                onAddMovementClick,
                onEditMovementClick,
                onDeleteMovementClick,
                onLatestOrBestResultsSwitchChecked,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun SuccessUi(
    movementListUiState: Success,
    onMovementClick: (Movement, Lifecycle.State) -> Unit,
    setAnalyticsCollectionEnabled: (Boolean) -> Unit,
    onAddMovementClick: (Movement, WeightUnit) -> Unit,
    onEditMovementClick: (Movement) -> Unit,
    onDeleteMovementClick: (Long) -> Unit,
    onLatestOrBestResultsSwitchChecked: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val analyticsService = LocalAnalyticsService.current

    var movementToEdit by remember { mutableStateOf<Movement?>(null) }
    var showAddMovementDialog by remember { mutableStateOf(false) }
    var movementToDelete by remember { mutableStateOf<Movement?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (movementListUiState.movements.isEmpty()) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.movement_list_screen_empty_list),
                    style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold),
                )
            }
        } else {
            Box(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.semantics {
                    contentDescription =
                        context.getString(R.string.movement_list_screen_latest_or_best_results_switch_content_description)
                },
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Latest", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Switch(
                    checked = movementListUiState.showBestResults,
                    onCheckedChange = onLatestOrBestResultsSwitchChecked,
                )
                Text(text = "Best", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
            }
            Box(modifier = Modifier.height(24.dp))

            LazyColumn(
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
                                analyticsService.logMovementListDropDownMenu_EditClick(movement)
                                movementToEdit = movement
                            },
                            onDeleteMovementClick = { movement ->
                                analyticsService.logMovementListDropDownMenu_DeleteClick(movement)
                                movementToDelete = movement
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                modifier = Modifier
                    .semantics {
                        contentDescription =
                            context.getString(R.string.movement_list_screen_add_movement_button_content_description)
                    },
                onClick = {
                    analyticsService.logMovementList_AddMovementButtonClick()
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

@Composable
private fun LoadingUi() {
    val context = LocalContext.current

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
    val analyticsService = LocalAnalyticsService.current

    Card(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    analyticsService.logMovementList_MovementClick(movement)
                    onMovementClick(movement, lifecycleOwner.lifecycle.currentState)
                },
                onLongClick = {
                    analyticsService.logMovementList_MovementLongClick(movement)
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
                    text = stringResource(
                        R.string.weight_with_unit,
                        movement.weight.multiplyIfPoundsAndRoundToNearestQuarter(weightUnit),
                        weightUnit.toString(context),
                    ),
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
                    showBestResults = false,
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
                    showBestResults = false,
                ),
            )
        }
    }
}