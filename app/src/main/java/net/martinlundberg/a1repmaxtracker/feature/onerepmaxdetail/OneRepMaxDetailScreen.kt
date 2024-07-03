package net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
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
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

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
        oneRepMaxDetailUiState = oneRepMaxDetailUiState,
        movementName = movementName
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRepMaxDetailScreen(
    oneRepMaxDetailUiState: OneRepMaxDetailUiState = Loading,
    movementName: String = "",
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = movementName) },
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
                var weightText by remember { mutableStateOf("${oneRepMaxDetailUiState.oneRMInfo.weight} kg") }
                var notesText by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(vertical = 8.dp),
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
                        Column {
                            Text(text = "Date")
                            CustomDatePicker(oneRepMaxDetailUiState.oneRMInfo.date)
                        }
                        Column {
                            Text(text = "Time")
                            // TODO: add time picker
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker(currentDate: OffsetDateTime) {
    var date by remember { mutableStateOf(currentDate.toLocalDate()) }
    var showDialog by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            modifier = Modifier.clickable {
                showDialog = true
            },
            enabled = false,
            value = date.format(DateTimeFormatter.ISO_DATE),
            onValueChange = {},
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
    }

    if (showDialog) {
        CustomDatePickerDialog(
            onAccept = {
                if (it != null) {
                    date = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDate()

                    // TODO: Save in room
                }
                showDialog = false
            },
            onCancel = {
                showDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit,
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Accept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@Preview(showBackground = true)
@Composable
fun OneRepMaxDetailScreenLoadingPreview() {
    _1RepMaxTrackerTheme {
        OneRepMaxDetailScreen(
            oneRepMaxDetailUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OneRepMaxDetailScreenSuccessPreview() {
    _1RepMaxTrackerTheme {
        OneRepMaxDetailScreen(
            oneRepMaxDetailUiState = Success(
                oneRMInfo = OneRMInfo(
                    id = 1,
                    weight = 100,
                    date = OffsetDateTime.of(2024, 9, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                )
            ),
        )
    }
}