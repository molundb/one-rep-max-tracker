package net.martinlundberg.onerepmaxtracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme
import net.martinlundberg.onerepmaxtracker.ui.theme.White
import net.martinlundberg.onerepmaxtracker.util.formatTo
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Composable
fun OutlinedTextFieldDatePicker(
    currentDateTime: OffsetDateTime,
    showDialog: Boolean,
    textStyle: TextStyle,
    setDialogVisibility: (Boolean) -> Unit = {},
    onAccept: (OffsetDateTime) -> Unit = {},
) {
    OutlinedTextField(
        modifier = Modifier
            .clickable { setDialogVisibility(true) }
            .semantics { contentDescription = "Outlined Text Field Date Picker" },
        enabled = false,
        value = currentDateTime.formatTo("dd MMM yyyy"),
//        value = DateUtils.getRelativeDateTimeString(
//            LocalContext.current,
//            currentDateTime.toInstant().toEpochMilli(),
//            DateUtils.DAY_IN_MILLIS,
//            DateUtils.WEEK_IN_MILLIS,
//            0
//        ).toString(),
        onValueChange = {},
        textStyle = textStyle,
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
                    val selectedDate = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneOffset.UTC)
                        .toLocalDateTime()

                    onAccept(
                        currentDateTime
                            .withMonth(selectedDate.monthValue)
                            .withDayOfMonth(selectedDate.dayOfMonth)
                    )
                }
                setDialogVisibility(false)
            },
            onCancel = {
                setDialogVisibility(false)
            },
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

    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            onSurface = Black,
        )
    ) {
        DatePickerDialog(
            modifier = Modifier.semantics { contentDescription = "Date Picker Dialog" },
            onDismissRequest = onCancel,
            confirmButton = {
                Button(
                    onClick = { onAccept(state.selectedDateMillis) },
                ) {
                    Text("Accept")
                }
            },
            dismissButton = {
                Button(
                    onClick = onCancel
                ) {
                    Text("Cancel")
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = White,
            )
        ) {
            DatePicker(state = state)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedTextFieldDatePickerPreview() {
    OneRepMaxTrackerTheme {
        OutlinedTextFieldDatePicker(
            currentDateTime = OffsetDateTime.now(),
            showDialog = false,
            MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedTextFieldDatePickerDialogPreview() {
    OneRepMaxTrackerTheme {
        OutlinedTextFieldDatePicker(
            currentDateTime = OffsetDateTime.now(),
            showDialog = true,
            textStyle = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedTextFieldDatePickerDialogPreview2() {
    OutlinedTextFieldDatePicker(
        currentDateTime = OffsetDateTime.now(),
        showDialog = true,
        textStyle = MaterialTheme.typography.bodyLarge,
    )

}