package net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail.OneRepMaxDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.ui.components.OutlinedTextFieldDatePicker
import net.martinlundberg.a1repmaxtracker.ui.components.OutlinedTextFieldTimePicker
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.weightUnit
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun OneRepMaxDetailRoute(
    oneRepMaxId: Long,
    movementName: String,
    oneRepMaxDetailViewModel: OneRepMaxDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        oneRepMaxDetailViewModel.getOneRepMaxDetail(oneRepMaxId)
    }

    val oneRepMaxDetailUiState by oneRepMaxDetailViewModel.uiState.collectAsState()
    OneRepMaxDetailScreen(
        oneRepMaxId = oneRepMaxId,
        oneRepMaxDetailUiState = oneRepMaxDetailUiState,
        movementName = movementName,
        updateOneRepMaxDetail = oneRepMaxDetailViewModel::updateOneRepMaxDetail,
        onDeleteClick = oneRepMaxDetailViewModel::deleteOneRM,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRepMaxDetailScreen(
    oneRepMaxId: Long,
    movementName: String,
    oneRepMaxDetailUiState: OneRepMaxDetailUiState = Loading,
    updateOneRepMaxDetail: (OneRMInfo) -> Unit = {},
    onDeleteClick: (Long) -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = movementName) },
                actions = {
                    IconButton(
                        onClick = { onDeleteClick(oneRepMaxId) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        when (oneRepMaxDetailUiState) {
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
                            .width(64.dp)
                            .semantics { contentDescription = "Circular Progress Indicator" },
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is Success -> {
                var weightText by remember { mutableStateOf("${oneRepMaxDetailUiState.oneRMInfo.weight} $weightUnit") }
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
                                currentDateTime = oneRepMaxDetailUiState.oneRMInfo.offsetDateTime,
                                showDialog = showDatePickerDialog,
                                setDialogVisibility = { showDatePickerDialog = it },
                                updateOneRepMaxDetail = { offsetDateTime ->
                                    updateOneRepMaxDetail(
                                        oneRepMaxDetailUiState.oneRMInfo.copy(offsetDateTime = offsetDateTime),
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
                                currentDateTime = oneRepMaxDetailUiState.oneRMInfo.offsetDateTime,
                                showDialog = showTimePickerDialog,
                                setDialogVisibility = { showTimePickerDialog = it },
                                updateOneRepMaxDetail = { offsetDateTime ->
                                    updateOneRepMaxDetail(
                                        oneRepMaxDetailUiState.oneRMInfo.copy(offsetDateTime = offsetDateTime),
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

@Preview(showBackground = true)
@Composable
private fun OneRepMaxDetailScreenLoadingPreview() {
    _1RepMaxTrackerTheme {
        OneRepMaxDetailScreen(
            oneRepMaxId = 0,
            movementName = "Back Squat",
            oneRepMaxDetailUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OneRepMaxDetailScreenSuccessPreview() {
    _1RepMaxTrackerTheme {
        OneRepMaxDetailScreen(
            oneRepMaxId = 0,
            movementName = "The name",
            oneRepMaxDetailUiState = Success(
                oneRMInfo = OneRMInfo(
                    id = 1,
                    movementId = 15,
                    weight = 100,
                    offsetDateTime = OffsetDateTime.of(2024, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC),
                )
            ),
        )
    }
}