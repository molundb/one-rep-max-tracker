package net.martinlundberg.a1repmaxtracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.martinlundberg.a1repmaxtracker.util.formatTo
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun OutlinedTextFieldDatePicker(
    currentDate: OffsetDateTime,
    showDialog: Boolean,
    setDialogVisibility: (Boolean) -> Unit = {},
    updateOneRepMaxDetail: (OffsetDateTime) -> Unit = {},
) {
    var date by remember { mutableStateOf(currentDate.toLocalDateTime()) }

    OutlinedTextField(
        modifier = Modifier.clickable {
            setDialogVisibility(true)
        },
        enabled = false,
        value = date.formatTo("dd MMM yyyy"),
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

    if (showDialog) {
        CustomDatePickerDialog(
            onAccept = {
                if (it != null) {
                    date = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime()

                    updateOneRepMaxDetail(OffsetDateTime.of(date, currentDate.offset))
                }
                setDialogVisibility(false)
            },
            onCancel = {
                setDialogVisibility(false)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomDatePickerDialog(
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
private fun OutlinedTextFieldDatePickerPreview() {
    OutlinedTextFieldDatePicker(
        currentDate = OffsetDateTime.now(),
        showDialog = false,
    )
}

@Preview(showBackground = true)
@Composable
private fun OutlinedTextFieldDatePickerDialogPreview() {
    OutlinedTextFieldDatePicker(
        currentDate = OffsetDateTime.now(),
        showDialog = true,
    )
}