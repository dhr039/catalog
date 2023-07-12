package com.cannonades.petconnect.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.cannonades.petconnect.R

@Composable
fun MyArrayDialog(
    array: Array<Int>,
    onArrayChange: (Array<Int>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val arrayState = remember { mutableStateOf(array) }
    val checkboxStates = remember { (1..6).map { i -> mutableStateOf(i in arrayState.value) } }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.categories_dialog),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                for (i in 1..6) {
                    CheckboxItem(
                        index = i,
                        checkedState = checkboxStates[i - 1],
                        onCheckedChange = { checked ->
                            checkboxStates[i - 1].value = checked
                            arrayState.value = checkboxStates.indices
                                .filter { checkboxStates[it].value }
                                .map { it + 1 }
                                .toTypedArray()
                            onArrayChange(arrayState.value)
                        }
                    )
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(id = R.string.dismiss_dialog),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Composable
fun CheckboxItem(
    index: Int,
    checkedState: MutableState<Boolean>,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Option $index",
            style = MaterialTheme.typography.bodySmall,
        )
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = onCheckedChange
        )
    }
}