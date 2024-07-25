package net.martinlundberg.onerepmaxtracker.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import net.martinlundberg.onerepmaxtracker.analytics.logAddResultDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddResultDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logAddResultDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteResultConfirmDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteResultConfirmDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteResultConfirmDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.analytics.logEditResultDialog_CancelClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditResultDialog_ConfirmClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditResultDialog_DeleteResultClick
import net.martinlundberg.onerepmaxtracker.analytics.logEditResultDialog_Dismissed
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.ui.components.OutlinedTextFieldDatePicker
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.ui.theme.White
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.kilosToPounds
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.POUNDS
import net.martinlundberg.onerepmaxtracker.util.removeTrailingZeros
import java.time.OffsetDateTime

@Composable
fun AddResultDialog(
    result: Result,
    weightUnit: WeightUnit,
    onDismissRequest: (Result) -> Unit = {},
    onConfirm: (Result) -> Unit = {},
    onCancel: (Result) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    AddOrEditResultDialog(
        result = result,
        weightUnit = weightUnit,
        cardContentDescription = stringResource(R.string.movement_detail_screen_add_result_dialog_content_description),
        title = stringResource(R.string.movement_detail_screen_add_result_dialog_title),
        confirmButtonText = stringResource(R.string.movement_detail_screen_add_result_dialog_confirm_button),
        confirmButtonContentDescription = stringResource(R.string.movement_detail_screen_add_result_dialog_confirm_button_content_description),
        onDismissRequest = { editedResult ->
            analyticsHelper.logAddResultDialog_Dismissed(editedResult)
            onDismissRequest(editedResult)
        },
        onConfirm = { editedResult ->
            analyticsHelper.logAddResultDialog_ConfirmClick(editedResult)
            onConfirm(editedResult)
        },
        onCancel = { editedResult ->
            analyticsHelper.logAddResultDialog_CancelClick(editedResult)
            onCancel(editedResult)
        },
    )
}

@Composable
fun EditResultDialog(
    result: Result,
    weightUnit: WeightUnit,
    onDismissRequest: (Result) -> Unit = {},
    onConfirm: (Result) -> Unit = {},
    onCancel: (Result) -> Unit = {},
    onDelete: (Result) -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    AddOrEditResultDialog(
        result = result,
        weightUnit = weightUnit,
        cardContentDescription = stringResource(R.string.movement_detail_screen_edit_result_dialog_content_description),
        title = stringResource(R.string.movement_detail_screen_edit_result_dialog_title),
        confirmButtonText = stringResource(R.string.save),
        confirmButtonContentDescription = stringResource(R.string.movement_detail_screen_edit_result_dialog_confirm_button_content_description),
        onDismissRequest = { editedResult ->
            analyticsHelper.logEditResultDialog_Dismissed(editedResult)
            onDismissRequest(editedResult)
        },
        onConfirm = { editedResult ->
            analyticsHelper.logEditResultDialog_ConfirmClick(editedResult)
            onConfirm(editedResult)
        },
        onCancel = { editedResult ->
            analyticsHelper.logEditResultDialog_CancelClick(editedResult)
            onCancel(editedResult)
        },
        onDelete = { editedResult ->
            analyticsHelper.logEditResultDialog_DeleteResultClick(editedResult.id)
            onDelete(editedResult)
        },
    )
}

@Composable
private fun AddOrEditResultDialog(
    result: Result,
    weightUnit: WeightUnit,
    cardContentDescription: String,
    title: String,
    confirmButtonText: String,
    confirmButtonContentDescription: String,
    onDismissRequest: (Result) -> Unit = {},
    onConfirm: (Result) -> Unit = {},
    onCancel: (Result) -> Unit = {},
    onDelete: ((Result) -> Unit)? = null,
) {
    val weightInitialValue = if (result.weight == 0f) {
        ""
    } else {
        if (weightUnit.isPounds()) {
            result.weight.kilosToPounds()
        } else {
            result.weight.removeTrailingZeros().toString()
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
    var commentText by remember { mutableStateOf(result.comment) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Dialog(onDismissRequest = {
        onDismissRequest(createResultOfInput(result, weightText, date, commentText))
    }) {
        val focusRequester = remember { FocusRequester() }
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
                Text(
                    stringResource(R.string.movement_detail_screen_add_or_edit_result_dialog_comment_label),
                    style = MaterialTheme.typography.titleMedium
                )
                Box(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = commentText,
                    onValueChange = { commentText = it },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                    maxLines = 10,
                )
                Box(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        onClick = {
                            val editedResult = createResultOfInput(result, weightText, date, commentText)

                            onCancel(editedResult)
                        },
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
                            val editedResult = createResultOfInput(result, weightText, date, commentText)
                            onConfirm(editedResult)
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
                            text = confirmButtonText,
                            modifier = Modifier.semantics {
                                contentDescription = confirmButtonContentDescription
                            },
                        )
                    }
                }
                Box(modifier = Modifier.height(24.dp))
                if (onDelete != null) {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        onClick = {
                            val editedResult = createResultOfInput(result, weightText, date, commentText)
                            onDelete(editedResult)
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

private fun createResultOfInput(
    result: Result,
    weightText: TextFieldValue,
    date: OffsetDateTime,
    commentText: String,
) = Result(
    id = result.id,
    movementId = result.movementId,
    weight = weightText.text.toFloatOrNull() ?: 0f,
    offsetDateTime = date,
    comment = commentText
)

@Composable
fun DeleteResultConfirmDialog(
    resultId: Long,
    movementName: String,
    weight: String,
    onDismissRequest: () -> Unit = {},
    onCancel: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    val analyticsHelper = LocalAnalyticsHelper.current
    ConfirmDeletionDialog(
        title = stringResource(R.string.delete_result_confirm_dialog_title),
        movementName = movementName,
        weight = weight,
        cardContentDescription = stringResource(R.string.delete_result_confirm_dialog_content_description),
        onDismissRequest = {
            analyticsHelper.logDeleteResultConfirmDialog_Dismissed(resultId)
            onDismissRequest()
        },
        onCancel = {
            analyticsHelper.logDeleteResultConfirmDialog_CancelClick(resultId)
            onCancel()
        },
        onConfirmation = {
            analyticsHelper.logDeleteResultConfirmDialog_ConfirmClick(resultId)
            onConfirmation()
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun AddResultDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        AddResultDialog(
            result = Result(
                movementId = 3,
                weight = 55f,
                offsetDateTime = OffsetDateTime.now(),
                comment = "Hey there. This is a longer comment testing what happens if there is a lot of text.\n\n" +
                          "Hey there. This is a longer comment testing what happens if there is a lot of text.\n\n" +
                          "Hey there. This is a longer comment testing what happens if there is a lot of text.",
            ),
            weightUnit = KILOGRAMS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddResultDialogDisabledPreview() {
    OneRepMaxTrackerTheme {
        AddResultDialog(
            result = Result(
                movementId = 1,
                weight = 0f,
                offsetDateTime = OffsetDateTime.now(),
                comment = "",
            ),
            weightUnit = POUNDS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EditResultDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        EditResultDialog(
            result = Result(
                movementId = 2,
                weight = 5f,
                offsetDateTime = OffsetDateTime.now(),
                comment = "Not much",
            ),
            weightUnit = POUNDS,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeleteResultConfirmDialogEnabledPreview() {
    OneRepMaxTrackerTheme {
        DeleteResultConfirmDialog(
            resultId = 5,
            movementName = "Movement Name",
            weight = "87.25 lb"
        )
    }
}

