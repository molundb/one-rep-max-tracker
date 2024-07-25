package net.martinlundberg.onerepmaxtracker.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.analytics.LocalAnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddMovementDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovementConfirmDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovementConfirmDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_DeleteMovementClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditMovementDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS

@Composable
fun DeleteMovementConfirmDialog(
    movementId: Long,
    movementName: String,
    onDismissRequest: () -> Unit = {},
    onCancel: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    ConfirmDeletionDialog(
        title = stringResource(R.string.delete_movement_confirm_dialog_title),
        movementName = movementName,
        cardContentDescription = stringResource(R.string.delete_movement_confirm_dialog_content_description),
        onDismissRequest = {
            analyticsHelper.logDeleteMovementConfirmDialog_Dismissed(movementId)
            onDismissRequest()
        },
        onCancel = {
            analyticsHelper.logDeleteMovementConfirmDialog_CancelClick(movementId)
            onCancel()
        },
        onConfirmation = onConfirmation,
    )
}

@Composable
fun AddMovementDialog(
    weightUnit: WeightUnit,
    onDismissRequest: (Movement) -> Unit = {},
    onCancel: (Movement) -> Unit = {},
    onConfirm: (Movement) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    AddOrEditMovementDialog(
        movement = Movement(name = ""),
        weightUnit = weightUnit,
        cardContentDescription = stringResource(R.string.movement_list_screen_add_movement_dialog_content_description),
        title = stringResource(R.string.movement_list_screen_add_movement_dialog_title),
        showWeightField = true,
        confirmButtonText = stringResource(R.string.movement_list_screen_add_movement_dialog_confirm_button),
        onDismissRequest = { movement ->
            analyticsHelper.logAddMovementDialog_Dismissed(movement)
            onDismissRequest(movement)
        },
        onCancel = { movement ->
            analyticsHelper.logAddMovementDialog_CancelClick(movement)
            onCancel(movement)
        },
        onConfirm = { movement ->
            analyticsHelper.logAddMovementDialog_ConfirmClick(movement)
            onConfirm(movement)
        },
    )
}

@Composable
fun EditMovementDialog(
    movement: Movement,
    weightUnit: WeightUnit,
    onDismissRequest: (Movement) -> Unit = {},
    onCancel: (Movement) -> Unit = {},
    onConfirm: (Movement) -> Unit = { },
    onDelete: (Movement) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    AddOrEditMovementDialog(
        movement = movement,
        weightUnit = weightUnit,
        cardContentDescription = stringResource(R.string.movement_list_screen_edit_movement_dialog_content_description),
        title = stringResource(R.string.movement_list_screen_edit_movement_dialog_title),
        showWeightField = false,
        confirmButtonText = stringResource(R.string.save),
        onDismissRequest = { editedMovement ->
            analyticsHelper.logEditMovementDialog_Dismissed(editedMovement)
            onDismissRequest(editedMovement)
        },
        onCancel = { editedMovement ->
            analyticsHelper.logEditMovementDialog_CancelClick(editedMovement)
            onCancel(editedMovement)
        },
        onConfirm = { editedMovement ->
            analyticsHelper.logEditMovementDialog_ConfirmClick(editedMovement)
            onConfirm(editedMovement)
        },
        onDelete = { editedMovement ->
            analyticsHelper.logEditMovementDialog_DeleteMovementClick(editedMovement)
            onDelete(editedMovement)
        },
    )
}

@Composable
private fun AddOrEditMovementDialog(
    movement: Movement,
    weightUnit: WeightUnit,
    cardContentDescription: String,
    title: String,
    showWeightField: Boolean,
    confirmButtonText: String,
    onDismissRequest: (Movement) -> Unit = {},
    onCancel: (Movement) -> Unit = {},
    onConfirm: (Movement) -> Unit = { },
    onDelete: ((Movement) -> Unit)? = null,
) {
    var movementNameText by remember { mutableStateOf(TextFieldValue(movement.name, TextRange(movement.name.length))) }
    val weightInitialValue = movement.weight?.toString() ?: ""
    var movementWeightText by remember { mutableStateOf(weightInitialValue) }

    Dialog(onDismissRequest = {
        onDismissRequest(
            createMovementOfInput(
                movementId = movement.id,
                movementNameText = movementNameText.text,
                movementWeightText = movementWeightText,
            ),
        )
    }) {
        val focusRequester = remember { FocusRequester() }
        val context = LocalContext.current
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = cardContentDescription
                },
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge
                )
                Box(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.movement_list_screen_add_or_edit_movement_dialog_name_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Box(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = movementNameText,
                    onValueChange = { movementNameText = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .semantics {
                            contentDescription =
                                context.getString(R.string.movement_list_screen_add_or_edit_movement_dialog_name_field_content_description)
                        },
                )
                if (showWeightField) {
                    Spacer(modifier = Modifier.size(24.dp))
                    Text(
                        text = stringResource(
                            R.string.movement_list_screen_add_or_edit_movement_dialog_weight_label,
                            weightUnit.toString(LocalContext.current)
                        ),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Box(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = movementWeightText,
                        onValueChange = { movementWeightText = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
                Box(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = {
                            onCancel(
                                createMovementOfInput(
                                    movementId = movement.id,
                                    movementNameText = movementNameText.text,
                                    movementWeightText = movementWeightText,
                                ),
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Black,
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
                            onConfirm(
                                createMovementOfInput(
                                    movementId = movement.id,
                                    movementNameText = movementNameText.text,
                                    movementWeightText = movementWeightText,
                                ),
                            )
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            disabledContentColor = Color.White,
                            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        ),
                        enabled = movementNameText.text.isNotBlank()
                    ) {
                        Text(confirmButtonText)
                    }
                }
                Box(modifier = Modifier.height(24.dp))
                if (onDelete != null) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
                            val editedMovement = createMovementOfInput(
                                movementId = movement.id,
                                movementNameText = movementNameText.text,
                                movementWeightText = movementWeightText,
                            )
                            onDelete(editedMovement)
                        },
                    ) {
                        Text(text = stringResource(R.string.movement_list_screen_edit_movement_dialog_delete_button))
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

private fun createMovementOfInput(
    movementId: Long,
    movementNameText: String,
    movementWeightText: String,
) = Movement(
    id = movementId,
    name = movementNameText,
    weight = movementWeightText.toFloatOrNull()
)

@Preview(showBackground = true)
@Composable
private fun AddMovementDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        AddMovementDialog(
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMovementDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        EditMovementDialog(
            movement = Movement(id = 1, name = "Movement 1", weight = 100.75f),
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditMovementDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        EditMovementDialog(
            movement = Movement(id = 1, name = "", weight = 55.75f),
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteMovementConfirmDialogPreview() {
    OneRepMaxTrackerTheme {
        DeleteMovementConfirmDialog(
            movementId = 5,
            movementName = "Movement 1",
        )
    }
}