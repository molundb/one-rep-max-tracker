package net.martinlundberg.onerepmaxtracker.ui.components.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.ui.model.MovementUiModel
import net.martinlundberg.onerepmaxtracker.ui.theme.Black
import net.martinlundberg.onerepmaxtracker.ui.theme.White

@Composable
fun MovementDropDownMenu(
    movement: MovementUiModel,
    onEditMovementClick: (MovementUiModel) -> Unit = {},
    onDeleteMovementClick: (MovementUiModel) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            surface = White,
            onSurface = Black,
        )
    ) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopEnd)
                .semantics {
                    contentDescription =
                        context.getString(R.string.movement_list_screen_movement_drop_down_menu_content_description)
                }
        ) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {
                    onDismiss()
                }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit)) },
                    onClick = {
                        onDismiss()
                        onEditMovementClick(movement)
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete)) },
                    onClick = {
                        onDismiss()
                        onDeleteMovementClick(movement)
                    }
                )
            }
        }
    }
}