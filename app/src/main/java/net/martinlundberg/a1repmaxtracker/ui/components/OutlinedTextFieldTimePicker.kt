package net.martinlundberg.a1repmaxtracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.martinlundberg.a1repmaxtracker.util.formatTo
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldTimePicker(
    currentDateTime: OffsetDateTime,
    showDialog: Boolean,
    setDialogVisibility: (Boolean) -> Unit = {},
    updateOneRepMaxDetail: (OffsetDateTime) -> Unit = {},
) {
    val timePickerState = rememberTimePickerState(currentDateTime.hour, currentDateTime.minute)

    OutlinedTextField(
        modifier = Modifier
            .clickable { setDialogVisibility(true) }
            .semantics { contentDescription = "Outlined Text Field Time Picker" },
        enabled = false,
        value = currentDateTime.formatTo("hh:mm a"),
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
        TimePickerDialog(
            onConfirm = {
                updateOneRepMaxDetail(currentDateTime.withHour(timePickerState.hour).withMinute(timePickerState.minute))
                setDialogVisibility(false)
            },
            onCancel = {
                setDialogVisibility(false)
            },
        ) {
            TimePicker(
                modifier = Modifier.semantics { contentDescription = "Time Picker Dialog" },
                state = timePickerState,
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit = {},
    onConfirm: () -> Unit = {},
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("Accept") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OutlinedTextFieldTimePickerPreview() {
    OutlinedTextFieldTimePicker(
        currentDateTime = OffsetDateTime.now(),
        showDialog = false,
    )
}

@Preview
@Composable
private fun OutlinedTextFieldTimePickerDialogPreview() {
    OutlinedTextFieldTimePicker(
        currentDateTime = OffsetDateTime.now(),
        showDialog = true,
    )
}


