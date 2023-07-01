package com.cannonades.petconnect.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.cannonades.petconnect.R

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier
) {

    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Divider()
            Row {
                Text(text = stringResource(id = R.string.dark_light_theme))
                Spacer(modifier = modifier.width(10.dp))
                ThemeToggleButton(
                    onThemeChange = onThemeChange,
                    modifier = modifier
                )
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
fun ThemeToggleButton(
    onThemeChange: () -> Unit,
    modifier: Modifier
) {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_moon),
        contentDescription = stringResource(id = R.string.change_theme),
        modifier = modifier
            .clickable(onClick = { onThemeChange() }),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}