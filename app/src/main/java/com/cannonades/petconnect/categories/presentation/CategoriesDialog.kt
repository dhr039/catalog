package com.cannonades.petconnect.categories.presentation

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.presentation.model.UICategory

@Composable
fun CategoriesDialog(
    onDismiss: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val categoriesUiState by viewModel.categoriesUiState.collectAsStateWithLifecycle()

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
                categoriesUiState.categories.forEach { category ->
                    CheckboxItem(
                        category = category,
                        onCheckedChange = { _ ->
                            viewModel.onCategoryChecked(category)
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
    category: UICategory,
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
            text = category.name,
            style = MaterialTheme.typography.bodySmall,
        )
        Checkbox(
            checked = category.checked,
            onCheckedChange = onCheckedChange
        )
    }
}