package net.martinlundberg.onerepmaxtracker.feature.movementdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import net.martinlundberg.onerepmaxtracker.DefaultScaffold
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.LocalAnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.TrackScreenViewEvent
import net.martinlundberg.onerepmaxtracker.analytics.logMovementDetail_AddButtonClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementDetail_EditButtonClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementDetail_NavBackClick
import net.martinlundberg.onerepmaxtracker.analytics.logMovementDetail_ResultClick
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.NoMovementDetail
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.AddResultDialog
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.DeleteMovementConfirmDialog
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.DeleteResultConfirmDialog
import net.martinlundberg.onerepmaxtracker.ui.components.dialogs.EditMovementDialog
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.multiplyIfPoundsAndRoundToNearestQuarter
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun MovementDetailRoute(
    innerPadding: PaddingValues,
    movementId: Long,
    movementName: String,
    onResultClick: (Long, String, Lifecycle.State) -> Unit = { _, _, _ -> },
    navigateBack: (Lifecycle.State) -> Unit = {},
    movementDetailViewModel: MovementDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        movementDetailViewModel.getMovementInfo(movementId)
    }
    val movementDetailUiState by movementDetailViewModel.uiState.collectAsState(Loading(MovementDetail(movementName)))

    MovementDetailScreen(
        innerPadding = innerPadding,
        movementId = movementId,
        movementDetailUiState = movementDetailUiState,
        onResultClick = onResultClick,
        navigateBack = navigateBack,
        addResult = movementDetailViewModel::addResult,
        onEditMovementClick = movementDetailViewModel::editMovement,
        onDeleteMovementClick = movementDetailViewModel::deleteMovement,
        onDeleteResultClick = movementDetailViewModel::deleteResult,
    )
}

@Composable
fun MovementDetailScreen(
    innerPadding: PaddingValues,
    movementId: Long,
    movementDetailUiState: MovementDetailUiState,
    onResultClick: (Long, String, Lifecycle.State) -> Unit = { _, _, _ -> },
    navigateBack: (Lifecycle.State) -> Unit = {},
    addResult: (result: Result, weightUnit: WeightUnit) -> Unit = { _, _ -> },
    onEditMovementClick: (Movement) -> Unit = {},
    onDeleteMovementClick: (Long) -> Unit = {},
    onDeleteResultClick: (Long) -> Unit = {},
) {
    TrackScreenViewEvent(screenName = "MovementDetail")

    val lifecycleOwner = LocalLifecycleOwner.current
    val analyticsHelper = LocalAnalyticsHelper.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(all = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = movementDetailUiState.movement?.movementName ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                )
            }
            Box(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            analyticsHelper.logMovementDetail_NavBackClick()
                            navigateBack(lifecycleOwner.lifecycle.currentState)
                        }
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.back_button_content_description)
                )
            }
        }

        when (movementDetailUiState) {
            is Loading -> LoadingUi()
            is Success -> SuccessUi(
                movementDetailUiState,
                movementId,
                analyticsHelper,
                onResultClick,
                addResult,
                onDeleteResultClick,
                onEditMovementClick,
                onDeleteMovementClick,
            )

            is NoMovementDetail -> {
                navigateBack(lifecycleOwner.lifecycle.currentState)
            }
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

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun SuccessUi(
    movementDetailUiState: Success,
    movementId: Long,
    analyticsHelper: AnalyticsHelper,
    onResultClick: (Long, String, Lifecycle.State) -> Unit,
    addResult: (result: Result, weightUnit: WeightUnit) -> Unit,
    onDeleteResultClick: (Long) -> Unit,
    onEditMovementClick: (Movement) -> Unit,
    onDeleteMovementClick: (Long) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var resultToDelete by remember { mutableStateOf<Result?>(null) }
    var showAddResultDialog by remember { mutableStateOf(false) }
    var showEditMovementDialog by remember { mutableStateOf(false) }
    var showDeleteMovementConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            movementDetailUiState.movement.results.map { result ->
                item(key = result.id) {
                    ResultCard(
                        modifier = Modifier.animateItemPlacement(),
                        result = result,
                        weightUnit = movementDetailUiState.weightUnit,
                        onResultClick = {
                            analyticsHelper.logMovementDetail_ResultClick(result)
                            onResultClick(
                                result.id,
                                movementDetailUiState.movement.movementName,
                                lifecycleOwner.lifecycle.currentState,
                            )
                        },
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .width(120.dp)
                    .semantics {
                        contentDescription =
                            context.getString(R.string.movement_detail_screen_edit_movement_button_content_description)
                    },
                onClick = {
                    analyticsHelper.logMovementDetail_EditButtonClick(
                        movementId,
                        movementDetailUiState.movement.movementName
                    )
                    showEditMovementDialog = true
                },
            ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 12.dp
                    ),
                    text = stringResource(R.string.edit),
                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                )
            }
            Button(
                modifier = Modifier
                    .semantics {
                        contentDescription =
                            context.getString(R.string.movement_detail_screen_add_result_button_content_description)
                    },
                onClick = {
                    analyticsHelper.logMovementDetail_AddButtonClick(
                        movementId,
                        movementDetailUiState.movement.movementName
                    )
                    showAddResultDialog = true
                },
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = stringResource(R.string.movement_detail_screen_add_result_button),
                )
            }
        }

        if (showAddResultDialog) {
            AddResultDialog(
                result = Result(
                    movementId = movementId,
                    weight = 0f,
                    offsetDateTime = movementDetailUiState.currentOffsetDateTime,
                    comment = "",
                ),
                weightUnit = movementDetailUiState.weightUnit,
                onDismissRequest = {
                    showAddResultDialog = false
                },
                onConfirm = { editedResult ->
                    addResult(editedResult, movementDetailUiState.weightUnit)
                    showAddResultDialog = false
                },
                onCancel = { showAddResultDialog = false },
            )
        }

        resultToDelete?.let { result ->
            DeleteResultConfirmDialog(
                resultId = result.id,
                movementName = movementDetailUiState.movement.movementName,
                weight = stringResource(
                    R.string.weight_with_unit,
                    result.weight.multiplyIfPoundsAndRoundToNearestQuarter(movementDetailUiState.weightUnit),
                    movementDetailUiState.weightUnit.toString(context),
                ),
                onDismissRequest = {
                    resultToDelete = null
                },
                onCancel = {
                    resultToDelete = null
                },
                onConfirmation = {
                    onDeleteResultClick(result.id)
                    resultToDelete = null
                }
            )
        }

        if (showEditMovementDialog) {
            EditMovementDialog(
                movement = Movement(movementId, movementDetailUiState.movement.movementName),
                weightUnit = movementDetailUiState.weightUnit,
                onDismissRequest = {
                    showEditMovementDialog = false
                },
                onCancel = {
                    showEditMovementDialog = false
                },
                onConfirm = { editedMovement ->
                    onEditMovementClick(editedMovement)
                    showEditMovementDialog = false
                },
                onDelete = {
                    showDeleteMovementConfirmDialog = true
                    showEditMovementDialog = false
                }
            )
        }

        if (showDeleteMovementConfirmDialog) {
            DeleteMovementConfirmDialog(
                movementId = movementId,
                movementName = movementDetailUiState.movement.movementName,
                onDismissRequest = {
                    showDeleteMovementConfirmDialog = false
                },
                onCancel = {
                    showDeleteMovementConfirmDialog = false
                },
                onConfirmation = {
                    onDeleteMovementClick(movementId)
                    showDeleteMovementConfirmDialog = false
                }
            )
        }
    }
}

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    result: Result,
    weightUnit: WeightUnit,
    onResultClick: ((Result) -> Unit)? = null,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.semantics {
            contentDescription = context.getString(R.string.movement_detail_screen_result_card_content_description)
        },
        onClick = {
            if (onResultClick != null) {
                onResultClick(result)
            }
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
            Text(
                text = stringResource(
                    R.string.weight_with_unit,
                    result.weight.multiplyIfPoundsAndRoundToNearestQuarter(weightUnit),
                    weightUnit.toString(context),
                ),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = result.dateTimeFormatted ?: "",
                style = MaterialTheme.typography.titleMedium
            )
            Box(modifier = Modifier.width(8.dp))
            if (onResultClick != null) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = context.getString(R.string.movement_list_screen_movement_card_nav_icon_content_description),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementDetailLoadingPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            MovementDetailScreen(
                innerPadding = innerPadding,
                movementId = 1,
                movementDetailUiState = Loading(movement = MovementDetail("Bench Press")),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementDetailScreenSuccessPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            val offsetDateTimeAndFormatted =
                OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC)
            MovementDetailScreen(
                innerPadding = innerPadding,
                movementId = 111L,
                movementDetailUiState = Success(
                    MovementDetail(
                        movementName = "Back Squat",
                        listOf(
                            Result(
                                id = 80,
                                movementId = 18,
                                weight = 15.5f,
                                offsetDateTime = offsetDateTimeAndFormatted,
                                comment = "This is a nice comment",
                            ),
                            Result(
                                id = 75,
                                movementId = 18,
                                weight = 15f,
                                offsetDateTime = offsetDateTimeAndFormatted,
                                comment = "Happiness comes from within",
                            ),
                            Result(
                                id = 70,
                                movementId = 18,
                                weight = 15f,
                                offsetDateTime = offsetDateTimeAndFormatted,
                                comment = "You are the universe experiencing itself",
                            ),
                        )
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                    currentOffsetDateTime = offsetDateTimeAndFormatted,
                ),
            )
        }
    }
}