package net.martinlundberg.a1repmaxtracker.feature.onerepmaxdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

@Composable
fun OneRepMaxDetailRoute() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRepMaxDetailScreen(
    movementId: Long = 0,
    movementName: String = "",
    oneRMInfo: OneRMInfo,
) {
    var weightText by remember { mutableStateOf("${oneRMInfo.weight} kg") }
    var notesText by remember { mutableStateOf("") }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                Column {
                    Text(text = "Weight")
                    TextField(
                        value = weightText,
                        onValueChange = { weightText = it }
                    )
                }
                // TODO: decide how to handle reps
                Column {
                    Text(text = "Reps")
                    Text(text = "1")
                }
            }
            Row {
                Column {
                    Text(text = "Date")
                    // TODO: add calendar picker
                }
                Column {
                    Text(text = "Time")
                    // TODO: add time picker
                }
            }
            Column {
                Text(text = "Notes")
                TextField(
                    value = notesText,
                    onValueChange = { notesText = it }
                )
            }
        }
    }
}