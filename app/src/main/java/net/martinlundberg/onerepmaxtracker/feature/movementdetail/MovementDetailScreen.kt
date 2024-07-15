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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
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
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.TrackScreenViewEvent
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.feature.movementslist.DeleteMovementConfirmDialog
import net.martinlundberg.onerepmaxtracker.ui.components.OutlinedTextFieldDatePicker
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.ui.theme.White
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.kilosToPounds
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.weightWithUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.toStringWithoutTrailingZero
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun MovementDetailRoute(
    innerPadding: PaddingValues,
    movementId: Long,
    movementName: String,
//    onOneRepMaxClick: (Long, String) -> Unit = { _, _ -> },
    navigateBack: (Lifecycle.State) -> Unit = {},
    movementDetailViewModel: MovementDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        movementDetailViewModel.getMovementInfo(movementId)
    }
    val movementDetailUiState by movementDetailViewModel.uiState.collectAsState()

    MovementDetailScreen(
        innerPadding = innerPadding,
        movementId = movementId,
        movementName = movementName,
        movementDetailUiState = movementDetailUiState,
//        onOneRepMaxClick = onOneRepMaxClick,
        navigateBack = navigateBack,
        addResult = movementDetailViewModel::addResult,
        onDeleteMovementClick = movementDetailViewModel::deleteMovement,
        onDeleteResultClick = movementDetailViewModel::deleteResult,
        getRelativeDateString = movementDetailViewModel::getRelativeDateString,
        analyticsHelper = movementDetailViewModel.analyticsHelper
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovementDetailScreen(
    innerPadding: PaddingValues,
    movementId: Long,
    movementName: String,
    movementDetailUiState: MovementDetailUiState = Loading,
    navigateBack: (Lifecycle.State) -> Unit = {},
    addResult: (result: Result, weightUnit: WeightUnit) -> Unit = { _, _ -> },
    onDeleteMovementClick: (Long) -> Unit = {},
    onDeleteResultClick: (Long) -> Unit = {},
    getRelativeDateString: (OffsetDateTime) -> String = { "" },
    analyticsHelper: AnalyticsHelper? = null,
) {
    analyticsHelper?.let { TrackScreenViewEvent(screenName = "MovementDetail", it) }

    var resultToEdit by remember { mutableStateOf<Result?>(null) }
    var showAddResultDialog by remember { mutableStateOf(false) }
    var showDeleteMovementConfirmDialog by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

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
                    text = movementName,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                )
            }
            Box(
                modifier = Modifier
                    .clickable(onClick = { navigateBack(lifecycleOwner.lifecycle.currentState) })
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.movement_detail_screen_back_button_content_description)
                )
            }
        }

        val context = LocalContext.current
        when (movementDetailUiState) {
            Loading -> {
                Box(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .semantics {
                            contentDescription =
                                context.getString(R.string.movement_detail_screen_loading_indicator_content_description)
                        },
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            is Success -> {
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
                                    onOneRepMaxClick = { resultToEdit = result },
                                    getRelativeDateString = getRelativeDateString,
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
                                        context.getString(R.string.movement_detail_screen_delete_result_button_content_description)
                                },
                            onClick = { showDeleteMovementConfirmDialog = true },
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    horizontal = 24.dp,
                                    vertical = 12.dp
                                ),
                                text = stringResource(R.string.delete),
                                style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                            )
                        }
                        Button(
                            modifier = Modifier
                                .semantics {
                                    contentDescription =
                                        context.getString(R.string.movement_detail_screen_add_result_button_content_description)
                                },
                            onClick = { showAddResultDialog = true },
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = stringResource(R.string.movement_detail_screen_add_result_button),
                            )
                        }
                    }

                    if (showAddResultDialog) {
                        AddOrEditResultDialog(
                            isAdd = true,
                            result = Result(
                                movementId = movementId,
                                weight = 0f,
                                offsetDateTime = OffsetDateTime.now()
                            ),
                            weightUnit = movementDetailUiState.weightUnit,
                            onDismissRequest = { showAddResultDialog = false },
                            onConfirmation = { result ->
                                addResult(result, movementDetailUiState.weightUnit)
                                showAddResultDialog = false
                            },
                        )
                    }

                    resultToEdit?.let {
                        AddOrEditResultDialog(
                            isAdd = false,
                            result = it,
                            weightUnit = movementDetailUiState.weightUnit,
                            onDismissRequest = { resultToEdit = null },
                            onConfirmation = { result ->
                                addResult(result, movementDetailUiState.weightUnit)
                                resultToEdit = null
                            },
                            onDeleteClicked = { resultId ->
                                onDeleteResultClick(resultId)
                                resultToEdit = null
                            },
                        )
                    }

                    if (showDeleteMovementConfirmDialog) {
                        DeleteMovementConfirmDialog(
                            movementName = movementName,
                            onDismissRequest = { showDeleteMovementConfirmDialog = false },
                            onConfirmation = {
                                onDeleteMovementClick(movementId)
                                showDeleteMovementConfirmDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    result: Result,
    weightUnit: WeightUnit,
    onOneRepMaxClick: (Result) -> Unit = { },
    getRelativeDateString: (OffsetDateTime) -> String = { "" },
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.semantics {
            contentDescription = context.getString(R.string.movement_detail_screen_result_card_content_description)
        },
        onClick = { onOneRepMaxClick(result) },
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
                result.weight.weightWithUnit(weightUnit.isPounds(), LocalContext.current),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = getRelativeDateString(result.offsetDateTime),
                style = MaterialTheme.typography.titleMedium
            )
            Box(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = context.getString(R.string.movement_list_screen_movement_card_nav_icon_content_description),
            )
        }
    }
}

@Composable
fun AddOrEditResultDialog(
    isAdd: Boolean,
    result: Result,
    weightUnit: WeightUnit,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Result) -> Unit = {},
    onDeleteClicked: (Long) -> Unit = {},
) {
    val weightInitialValue = if (result.weight == 0f) {
        ""
    } else {
        if (weightUnit.isPounds()) {
            result.weight.kilosToPounds()
        } else {
            result.weight.toStringWithoutTrailingZero()
        }
    }

    var weightText by remember {
        mutableStateOf(
            TextFieldValue(
                weightInitialValue,
                TextRange(weightInitialValue.length)
            )
        )
    }
    var date by remember { mutableStateOf(result.offsetDateTime) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Dialog(onDismissRequest = { onDismissRequest() }) {
        val focusRequester = remember { FocusRequester() }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription =
                        if (isAdd) context.getString(R.string.movement_detail_screen_add_result_dialog_content_description)
                        else context.getString(R.string.movement_detail_screen_edit_result_dialog_content_description)
                },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = if (isAdd) stringResource(R.string.movement_detail_screen_add_result_dialog_title)
                    else stringResource(R.string.movement_detail_screen_edit_result_dialog_title),
                    style = MaterialTheme.typography.headlineLarge,
                )
                Box(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(
                        R.string.movement_detail_screen_add_or_edit_result_dialog_weight_label,
                        weightUnit.toString(LocalContext.current)
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Box(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .semantics {
                            contentDescription =
                                context.getString(R.string.movement_detail_screen_add_or_edit_result_dialog_weight_field_content_description)
                        },
                )
                Box(modifier = Modifier.height(24.dp))
                Text(
                    stringResource(R.string.movement_detail_screen_add_or_edit_result_dialog_date_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Box(modifier = Modifier.height(12.dp))
                OutlinedTextFieldDatePicker(
                    currentDateTime = date,
                    showDialog = showDatePickerDialog,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    setDialogVisibility = { showDatePickerDialog = it },
                    onAccept = { offsetDateTime ->
                        date = offsetDateTime
                    },
                )
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
                            containerColor = White,
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
                            onConfirmation(
                                Result(
                                    id = result.id,
                                    movementId = result.movementId,
                                    weight = weightText.text.toFloat(),
                                    offsetDateTime = date,
                                )
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = White,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = White,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        ),
                        enabled = weightText.text.isNotBlank(),
                    ) {
                        Text(
                            text = if (isAdd) stringResource(R.string.movement_detail_screen_add_result_dialog_confirm_button)
                            else stringResource(R.string.movement_detail_screen_edit_result_dialog_confirm_button),
                            modifier = Modifier.semantics {
                                contentDescription =
                                    if (isAdd) context.getString(R.string.movement_detail_screen_add_result_dialog_confirm_button_content_description)
                                    else context.getString(R.string.movement_detail_screen_edit_result_dialog_confirm_button_content_description)
                            },
                        )
                    }
                }
                Box(modifier = Modifier.height(24.dp))
                if (!isAdd) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
                            onDeleteClicked(result.id)
                        },
                    ) {
                        Text(text = stringResource(R.string.movement_detail_screen_edit_result_dialog_delete_button))
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
private fun MovementDetailLoadingPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            MovementDetailScreen(
                innerPadding = innerPadding,
                movementId = 1,
                movementName = "Bench Press",
                movementDetailUiState = Loading,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MovementDetailScreenSuccessPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            MovementDetailScreen(
                innerPadding = innerPadding,
                movementId = 111L,
                movementName = "Back Squat",
                movementDetailUiState = Success(
                    MovementDetail(
                        listOf(
                            Result(
                                id = 80,
                                movementId = 18,
                                weight = 15.5f,
                                offsetDateTime = OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC)
                            ),
                            Result(
                                id = 75,
                                movementId = 18,
                                weight = 15f,
                                offsetDateTime = OffsetDateTime.of(2023, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC)
                            ),
                            Result(
                                id = 70,
                                movementId = 18,
                                weight = 15f,
                                offsetDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                            ),
                        )
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddResultDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditResultDialog(
            isAdd = true,
            result = Result(
                movementId = 3,
                weight = 0f,
                offsetDateTime = OffsetDateTime.now()
            ),
            weightUnit = WeightUnit.KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddResultDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditResultDialog(
            isAdd = true,
            result = Result(
                movementId = 1,
                weight = 0f,
                offsetDateTime = OffsetDateTime.now()
            ),
            weightUnit = WeightUnit.POUNDS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditResultDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditResultDialog(
            isAdd = false,
            result = Result(
                movementId = 2,
                weight = 5f,
                offsetDateTime = OffsetDateTime.now()
            ),
            weightUnit = WeightUnit.POUNDS,
        )
    }
}