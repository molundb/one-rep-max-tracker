package net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Loading
import net.martinlundberg.onerepmaxtracker.feature.onerepmaxdetail.ResultDetailUiState.Success
import net.martinlundberg.onerepmaxtracker.ui.components.OutlinedTextFieldDatePicker
import net.martinlundberg.onerepmaxtracker.ui.components.OutlinedTextFieldTimePicker
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.weightWithUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun ResultDetailRoute(
    innerPadding: PaddingValues,
    resultId: Long,
    movementName: String,
    resultDetailViewModel: ResultDetailViewModel = hiltViewModel(),
    navigateBack: (Lifecycle.State) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        resultDetailViewModel.getResult(resultId)
    }

    val resultDetailUiState by resultDetailViewModel.uiState.collectAsState()

    ResultDetailScreen(
        innerPadding = innerPadding,
        resultId = resultId,
        resultDetailUiState = resultDetailUiState,
        movementName = movementName,
        navigateBack = navigateBack,
        updateResultDetail = resultDetailViewModel::updateResult,
        onDeleteClick = resultDetailViewModel::deleteResult,
        setWeightUnitToPounds = resultDetailViewModel::setWeightUnit
    )
}

@Composable
fun ResultDetailScreen(
    innerPadding: PaddingValues,
    resultId: Long,
    movementName: String,
    resultDetailUiState: ResultDetailUiState,
    navigateBack: (Lifecycle.State) -> Unit = {},
    updateResultDetail: (Result) -> Unit = {},
    onDeleteClick: (Long) -> Unit = {},
    setWeightUnitToPounds: (Boolean) -> Unit = {},
) {
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
                    .clickable(
                        onClick = {
//                            analyticsHelper.logMovementDetail_NavBackClick()
                            navigateBack(lifecycleOwner.lifecycle.currentState)
                        }
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.movement_detail_screen_back_button_content_description)
                )
            }
        }

        val context = LocalContext.current
        when (resultDetailUiState) {
            is Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(64.dp)
                            .semantics {
                                contentDescription = context.getString(R.string.loading_indicator_content_description)
                            },
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is Success -> {
                var weightText by remember {
                    mutableStateOf(
                        resultDetailUiState.result.weight.weightWithUnit(
                            resultDetailUiState.weightUnit.isPounds(),
                            context,
                        )
                    )
                }
                var notesText by remember { mutableStateOf("") }
                var showDatePickerDialog by remember { mutableStateOf(false) }
                var showTimePickerDialog by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(all = 8.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row {
                        Column {
                            Text(text = "Weight")
                            TextField(
                                value = weightText,
                                onValueChange = { weightText = it }
                            )
                        }
                        // TODO: decide how to handle reps
                        Column {
                            Text(text = "Reps")
                            Text(text = "1")
                        }
                    }
                    Row {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Date")
                            OutlinedTextFieldDatePicker(
                                currentDateTime = resultDetailUiState.result.offsetDateTime,
                                showDialog = showDatePickerDialog,
                                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                                setDialogVisibility = { showDatePickerDialog = it },
                                onAccept = { offsetDateTime ->
                                    updateResultDetail(
                                        resultDetailUiState.result.copy(offsetDateTime = offsetDateTime),
                                    )
                                },
                            )
                        }
                        Spacer(Modifier.size(8.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Time")
                            OutlinedTextFieldTimePicker(
                                currentDateTime = resultDetailUiState.result.offsetDateTime,
                                showDialog = showTimePickerDialog,
                                setDialogVisibility = { showTimePickerDialog = it },
                                updateResultDetail = { offsetDateTime ->
                                    updateResultDetail(
                                        resultDetailUiState.result.copy(offsetDateTime = offsetDateTime),
                                    )
                                },
                            )
                        }
                    }
                }
                Column {
                    Text(text = "Notes")
                    TextField(
                        value = notesText,
                        onValueChange = { notesText = it }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ResultDetailScreenLoadingPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            ResultDetailScreen(
                innerPadding = innerPadding,
                resultId = 0,
                movementName = "Back Squat",
                resultDetailUiState = Loading,
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ResultDetailScreenSuccessPreview() {
    OneRepMaxTrackerTheme {
        DefaultScaffold { innerPadding ->
            ResultDetailScreen(
                innerPadding = innerPadding,
                resultId = 0,
                movementName = "Thrusters",
                resultDetailUiState = Success(
                    result = Result(
                        id = 1,
                        movementId = 15,
                        weight = 100.5f,
                        offsetDateTime = OffsetDateTime.of(2024, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                        comment = "Bring it on!",
                    ),
                    weightUnit = WeightUnit.KILOGRAMS,
                ),
            )
        }
    }
}