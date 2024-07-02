package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.compose.foundation.layout.Arrangement
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
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun MovementDetailRoute(
    movementId: Long,
    movementName: String,
    movementDetailViewModel: MovementDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        movementDetailViewModel.getMovementInfo(movementId)
    }
    val movementDetailUiState by movementDetailViewModel.uiState.collectAsState()
    MovementDetailScreen(
        movementDetailUiState = movementDetailUiState,
        movementId = movementId,
        movementName = movementName,
        add1RM = movementDetailViewModel::add1RM
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDetailScreen(
    movementDetailUiState: MovementDetailUiState = Loading,
    movementId: Long = 0,
    movementName: String = "",
    add1RM: (weight: Int, movementId: Long) -> Unit = { _, _ -> },
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
                                    weight = it.weight,
                                    date = it.formattedDate(),
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
    weight: Int?,
    date: String?,
) {
    Card(modifier = Modifier.semantics { contentDescription = "Movement Card" }) {
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
                Text("$weight kg", style = MaterialTheme.typography.titleLarge)
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
fun MovementDetailLoadingPreview() {
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementDetailUiState = Loading,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MovementDetailScreenContentPreview() {
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementDetailUiState = Success(
                MovementDetail(
                    listOf(
                        OneRMInfo(80, OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC)),
                        OneRMInfo(75, OffsetDateTime.of(2023, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC)),
                        OneRMInfo(70, OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)),
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Add1rmDialogPreview() {
    _1RepMaxTrackerTheme {
        Add1rmDialog()
    }
}