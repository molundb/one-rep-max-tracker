package net.martinlundberg.a1repmaxtracker.feature.movementdetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import net.martinlundberg.a1repmaxtracker.R
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Loading
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetailUiState.Success
import net.martinlundberg.a1repmaxtracker.feature.movementslist.DeleteMovementConfirmDialog
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
    navigateBack: () -> Unit = {},
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
        navigateBack = navigateBack,
        add1RM = movementDetailViewModel::add1RM,
        onDeleteMovementClick = movementDetailViewModel::deleteMovement,
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
    navigateBack: () -> Unit = {},
    add1RM: (weight: Float, weightUnit: String, movementId: Long) -> Unit = { _, _, _ -> },
    onDeleteMovementClick: (Long) -> Unit = {},
    setWeightUnitToPounds: (Boolean) -> Unit = {},
) {
    var showAdd1rmDialog by remember { mutableStateOf(false) }
    var showDeleteMovementConfirmDialog by rememberSaveable { mutableStateOf<Boolean>(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(top = 24.dp),
                title = {
                    Text(
                        text = "1RM Tracker",
                        style = MaterialTheme.typography.displayLarge,
                    )
                },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = weightUnit, style = MaterialTheme.typography.titleMedium)
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
    ) { innerPadding ->
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
                        .clickable(onClick = navigateBack)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back button"
                    )
                }
            }

            when (movementDetailUiState) {
                Loading -> {
                    Box(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(64.dp)
                            .semantics { contentDescription = "Circular Progress Indicator" },
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .width(120.dp)
                                    .semantics { contentDescription = "Add 1RM" },
                                onClick = { showDeleteMovementConfirmDialog = true },
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 24.dp,
                                        vertical = 12.dp
                                    ),
                                    text = "Delete",
                                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                                )
                            }
                            FloatingActionButton(
                                modifier = Modifier
                                    .width(120.dp)
                                    .semantics { contentDescription = "Add 1RM" },
                                onClick = { showAdd1rmDialog = true },
                                shape = RoundedCornerShape(80.dp),
                            ) {
                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 24.dp,
                                        vertical = 12.dp
                                    ),
                                    text = "+ Add new",
                                    style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                                )
                            }
                        }

                        if (showAdd1rmDialog) {
                            Add1rmDialog(
                                onDismissRequest = { showAdd1rmDialog = false },
                                onConfirmation = { weight ->
                                    add1RM(weight, weightUnit, movementId)
                                    showAdd1rmDialog = false
                                },
                                weightUnit = weightUnit,
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
}

@Composable
fun OneRMCard(
    id: Long,
    movementName: String,
    weight: Float,
    weightUnit: String,
    date: String,
    onOneRepMaxClick: (Long, String) -> Unit = { _, _ -> },
) {
    Card(
        modifier = Modifier.semantics { contentDescription = "Movement Card" },
        onClick = { onOneRepMaxClick(id, movementName) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(weight.weightWithUnit(weightUnit == "lb"), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(date, style = MaterialTheme.typography.titleMedium)
            Box(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Navigation icon",
            )
        }
    }
}

@Composable
fun Add1rmDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: (Float) -> Unit = {},
    initialWeight: String = "",
    weightUnit: String,
) {
    var weightText by remember { mutableStateOf(initialWeight) }

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
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = "Add new result",
                    style = MaterialTheme.typography.headlineLarge
                )
                Box(modifier = Modifier.height(24.dp))
                Text("Weight ($weightUnit)", style = MaterialTheme.typography.titleMedium)
                Box(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = weightText,
                    onValueChange = { weightText = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.focusRequester(focusRequester),
                )
                Box(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f),
                        onClick = { onDismissRequest() }
                    ) {
                        Text("Cancel")
                    }
                    Box(modifier = Modifier.width(32.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            onConfirmation(weightText.toFloat())
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        ),
                        enabled = weightText.isNotBlank(),
                    ) {
                        Text("Add result")
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
    _1RepMaxTrackerTheme {
        MovementDetailScreen(
            movementId = 1,
            movementName = "Bench Press",
            weightUnit = "kg",
            movementDetailUiState = Loading,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
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
                            weight = 15.5f,
                            offsetDateTime = OffsetDateTime.of(2023, 1, 5, 0, 0, 0, 0, ZoneOffset.UTC)
                        ),
                        OneRMInfo(
                            id = 75,
                            movementId = 18,
                            weight = 15f,
                            offsetDateTime = OffsetDateTime.of(2023, 1, 3, 0, 0, 0, 0, ZoneOffset.UTC)
                        ),
                        OneRMInfo(
                            id = 70,
                            movementId = 18,
                            weight = 15f,
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
private fun Add1rmDialogEnabledPreview() {
    _1RepMaxTrackerTheme {
        Add1rmDialog(
            initialWeight = "55",
            weightUnit = "kg"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Add1rmDialogDisabledPreview() {
    _1RepMaxTrackerTheme {
        Add1rmDialog(
            weightUnit = "lb"
        )
    }
}