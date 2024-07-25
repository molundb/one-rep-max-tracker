package net.martinlundberg.onerepmaxtracker.ui.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.OneRepMaxTrackerTheme

@Composable
fun ConfirmDeletionDialog(
    title: String,
    movementName: String,
    cardContentDescription: String,
    onCancel: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(224.dp)
                .semantics {
                    contentDescription = cardContentDescription
                },
            shape = RoundedCornerShape(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 16.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.confirm_dialog_are_you_sure),
                    style = MaterialTheme.typography.titleMedium,
                )
                Box(modifier = Modifier.height(12.dp))
                Text(
                    text = movementName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onCancel,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Black,
                        ),
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                    Box(modifier = Modifier.width(32.dp))
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onConfirmation() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White,
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) {
                        Text(stringResource(R.string.confirm_dialog_confirm_button))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ConfirmDeletionDialogPreview() {
    OneRepMaxTrackerTheme {
        ConfirmDeletionDialog(
            title = "Delete result",
            movementName = "Bench Press",
            cardContentDescription = "description",
        )
    }
}