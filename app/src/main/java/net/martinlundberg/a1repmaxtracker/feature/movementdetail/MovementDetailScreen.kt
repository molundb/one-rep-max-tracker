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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
import net.martinlundberg.a1repmaxtracker.ui.components.OutlinedTextFieldDatePicker
import net.martinlundberg.a1repmaxtracker.ui.theme.Black
import net.martinlundberg.a1repmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.a1repmaxtracker.ui.theme.White
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.kilosToPounds
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.weightWithUnit
import net.martinlundberg.a1repmaxtracker.util.formatTo
import net.martinlundberg.a1repmaxtracker.util.provideWeightUnitService
import net.martinlundberg.a1repmaxtracker.util.toStringWithoutTrailingZero
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun MovementDetailRoute(
    movementId: Long,
    movementName: String,
//    onOneRepMaxClick: (Long, String) -> Unit = { _, _ -> },
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
//        onOneRepMaxClick = onOneRepMaxClick,
        navigateBack = navigateBack,
        addResult = movementDetailViewModel::addResult,
        onDeleteMovementClick = movementDetailViewModel::deleteMovement,
        onDeleteResultClick = movementDetailViewModel::deleteResult,
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
    navigateBack: () -> Unit = {},
    addResult: (oneRMInfo: OneRMInfo, weightUnit: String, movementId: Long) -> Unit = { _, _, _ -> },
    onDeleteMovementClick: (Long) -> Unit = {},
    onDeleteResultClick: (Long) -> Unit = {},
    setWeightUnitToPounds: (Boolean) -> Unit = {},
) {
    var showAddResultDialog by rememberSaveable { mutableStateOf(false) }
    var oneRMInfoToEdit by rememberSaveable { mutableStateOf<OneRMInfo?>(null) }
    var showDeleteMovementConfirmDialog by rememberSaveable { mutableStateOf(false) }

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
                        Text(
                            text = weightUnit,
                            style = MaterialTheme.typography.titleMedium,
                            color = White,
                        )
                        Box(modifier = Modifier.size(4.dp))
                        Switch(
                            checked = weightUnit == "lb",
                            onCheckedChange = {
                                setWeightUnitToPounds(it)
                            },
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
                                        oneRMInfo = it,
                                        weightUnit = weightUnit,
                                        onOneRepMaxClick = { oneRMInfoToEdit = it },
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
                                    .semantics { contentDescription = "Delete result" },
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
                                    .semantics { contentDescription = "Add result" },
                                onClick = { showAddResultDialog = true },
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

                        if (showAddResultDialog) {
                            AddOrEditResultDialog(
                                isAdd = true,
                                oneRMInfo = OneRMInfo(
                                    movementId = movementId,
                                    weight = 0f,
                                    offsetDateTime = OffsetDateTime.now()
                                ),
                                weightUnit = weightUnit,
                                onDismissRequest = { showAddResultDialog = false },
                                onConfirmation = { weight ->
                                    addResult(weight, weightUnit, movementId)
                                    showAddResultDialog = false
                                },
                            )
                        }

                        oneRMInfoToEdit?.let {
                            AddOrEditResultDialog(
                                isAdd = false,
                                oneRMInfo = it,
                                weightUnit = weightUnit,
                                onDismissRequest = { oneRMInfoToEdit = null },
                                onConfirmation = { weight ->
                                    addResult(weight, weightUnit, movementId)
                                    oneRMInfoToEdit = null
                                },
                                onDeleteClicked = { resultId ->
                                    onDeleteResultClick(resultId)
                                    oneRMInfoToEdit = null
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
}

@Composable
fun OneRMCard(
    oneRMInfo: OneRMInfo,
    weightUnit: String,
    onOneRepMaxClick: (OneRMInfo) -> Unit = { },
) {
    Card(
        modifier = Modifier.semantics { contentDescription = "Movement Card" },
        onClick = { onOneRepMaxClick(oneRMInfo) },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(oneRMInfo.weight.weightWithUnit(weightUnit == "lb"), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(oneRMInfo.offsetDateTime.formatTo("dd MMM yyyy"), style = MaterialTheme.typography.titleMedium)
            Box(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "Navigation icon",
            )
        }
    }
}

@Composable
fun AddOrEditResultDialog(
    isAdd: Boolean,
    oneRMInfo: OneRMInfo,
    weightUnit: String,
    onDismissRequest: () -> Unit = {},
    onConfirmation: (OneRMInfo) -> Unit = {},
    onDeleteClicked: (Long) -> Unit = {},
) {
    val weightInitialValue = if (oneRMInfo.weight == 0f) {
        ""
    } else {
        if (weightUnit == "lb") {
            oneRMInfo.weight.kilosToPounds()
        } else {
            oneRMInfo.weight.toStringWithoutTrailingZero()
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
    var date by rememberSaveable { mutableStateOf(oneRMInfo.offsetDateTime) }
    var showDatePickerDialog by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        val focusRequester = remember { FocusRequester() }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = if (isAdd) "Add Result Dialog" else "Edit Result Dialog" },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = if (isAdd) "Add new result" else "Edit result",
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
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .semantics { contentDescription = "Weight Text Field" },
                )
                Box(modifier = Modifier.height(24.dp))
                Text("Date", style = MaterialTheme.typography.titleMedium)
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
                        Text("Cancel")
                    }
                    Box(modifier = Modifier.width(32.dp))
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = {
                            onConfirmation(
                                OneRMInfo(
                                    id = oneRMInfo.id,
                                    movementId = oneRMInfo.movementId,
                                    weight = weightText.text.toFloat(),
                                    offsetDateTime = date
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
                            text = if (isAdd) "Add result" else "Edit result",
                            modifier = Modifier.semantics {
                                contentDescription = if (isAdd) "Add result button" else "Edit result button"
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
                            onDeleteClicked(oneRMInfo.id)
                        },
                    ) {
                        Text(text = "Delete result")
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
    OneRepMaxTrackerTheme {
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
private fun AddResultDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditResultDialog(
            isAdd = true,
            oneRMInfo = OneRMInfo(
                movementId = 3,
                weight = 0f,
                offsetDateTime = OffsetDateTime.now()
            ),
            weightUnit = "kg"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddResultDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditResultDialog(
            isAdd = true,
            oneRMInfo = OneRMInfo(
                movementId = 1,
                weight = 0f,
                offsetDateTime = OffsetDateTime.now()
            ),
            weightUnit = "lb"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditResultDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        AddOrEditResultDialog(
            isAdd = false,
            oneRMInfo = OneRMInfo(
                movementId = 2,
                weight = 5f,
                offsetDateTime = OffsetDateTime.now()
            ),
            weightUnit = "lb"
        )
    }
}