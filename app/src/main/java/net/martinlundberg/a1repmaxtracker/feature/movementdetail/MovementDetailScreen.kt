package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.ui.theme._1RepMaxTrackerTheme
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.weightWithUnit
import net.martinlundberg.a1repmaxtracker.util.formatTo
import net.martinlundberg.a1repmaxtracker.util.provideWeightUnitService
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun MovementDetailRoute(
    movementId: Long,
    movementName: String,
    onOneRepMaxClick: (Long, String) -> Unit = { _, _ -> },
    movementDetailViewModel: MovementDetailViewModel = hiltViewModel(),
    weightUnitService: WeightUnitService = provideWeightUnitService(),
) {
    LaunchedEffect(Unit) {
        movementDetailViewModel.getMovementInfo(movementId)
    }
    val movementDetailUiState by movementDetailViewModel.uiState.collectAsState()
    val weightUnit by weightUnitService.weightUnitFlow.collectAsState()

    MovementDetailScreen(
        movementId = movementId,
        movementName = movementName,
        weightUnit = weightUnit,
        movementDetailUiState = movementDetailUiState,
        onOneRepMaxClick = onOneRepMaxClick,
        add1RM = movementDetailViewModel::add1RM,
        setWeightUnitToPounds = weightUnitService::setWeightUnitToPounds,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDetailScreen(
    movementId: Long,
    movementName: String,
    weightUnit: String,
    movementDetailUiState: MovementDetailUiState = Loading,
    onOneRepMaxClick: (Long, String) -> Unit = { _, _ -> },
    add1RM: (weight: Int, movementId: Long) -> Unit = { _, _ -> },
    setWeightUnitToPounds: (Boolean) -> Unit = {},
) {
    var showAdd1rmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = movementName) },
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
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        when (movementDetailUiState) {
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
                Column(
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
                        movementDetailUiState.movement.oneRMs.map {
                            item {
                                OneRMCard(
                                    movementName = movementName,
                                    id = it.id,
                                    weight = it.weight,
                                    weightUnit = weightUnit,
                                    date = it.offsetDateTime.formatTo("dd MMM yyyy"),
                                    onOneRepMaxClick = onOneRepMaxClick,
                                )
                            }
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier
                            .size(80.dp)
                            .semantics { contentDescription = "Add 1RM" },
                        onClick = { showAdd1rmDialog = true },
                    ) {
                        Icon(Filled.Add, "Floating action button.")
                    }
                    if (showAdd1rmDialog) {
                        Add1rmDialog(
                            onDismissRequest = { showAdd1rmDialog = false },
                            onConfirmation = { weight ->
                                add1RM(weight, movementId)
                                showAdd1rmDialog = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OneRMCard(
    id: Long,
    movementName: String,
    weight: Int?,
    weightUnit: String,
    date: String?,
    onOneRepMaxClick: (Long, String) -> Unit = { _, _ -> },
) {
    Card(
        modifier = Modifier.semantics { contentDescription = "Movement Card" },
        onClick = { onOneRepMaxClick(id, movementName) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (weight == null) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = "-", style = MaterialTheme.typography.titleLarge
                )
            } else {
                Text(weight.weightWithUnit(weightUnit == "lb"), style = MaterialTheme.typography.titleLarge)
            }
            if (date == null) {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = "-", style = MaterialTheme.typography.titleLarge
                )
            } else {
                Text(date, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
fun Add1rmDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Int) -> Unit = {},
) {
    var weightText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        val focusRequester = remember { FocusRequester() }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = "Add 1RM Dialog" },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
            ) {
                TextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    label = { Text("Weight") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.focusRequester(focusRequester),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation(weightText.toInt()) },
                        enabled = weightText.isNotBlank(),
                    ) {
                        Text("Add")
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
private fun MovementDetailLoadingPreview() {
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementId = 1,
            movementName = "Bench Press",
            weightUnit = "kg",
            movementDetailUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovementDetailScreenSuccessPreview() {
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementId = 111,
            movementName = "Back Squat",
            weightUnit = "kg",
            movementDetailUiState = Success(
                MovementDetail(
                    listOf(
                        OneRMInfo(
                            id = 80,
                            movementId = 18,
                            weight = 15,
                            offsetDateTime = OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC)
                        ),
                        OneRMInfo(
                            id = 75,
                            movementId = 18,
                            weight = 15,
                            offsetDateTime = OffsetDateTime.of(2023, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC)
                        ),
                        OneRMInfo(
                            id = 70,
                            movementId = 18,
                            weight = 15,
                            offsetDateTime = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                        ),
                    )
                )
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Add1rmDialogPreview() {
    _1RepMaxTrackerTheme {
        Add1rmDialog()
    }
}