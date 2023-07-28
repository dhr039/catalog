package com.cannonades.petconnect.feature.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.cannonades.petconnect.R

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    darkThemeConfig: DarkThemeConfig
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
            Column {
                Divider()
                Spacer(modifier = modifier.height(20.dp))
                Row {
                    Text(text = stringResource(id = R.string.dark_light_theme))
                    Spacer(modifier = modifier.width(10.dp))
                }
                Spacer(modifier = modifier.height(20.dp))

                SettingsDialogSectionTitle(text = stringResource(R.string.dark_mode_preference))
                Column(Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.dark_mode_config_system_default),
                        selected = darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.dark_mode_config_light),
                        selected = darkThemeConfig == DarkThemeConfig.LIGHT,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.dark_mode_config_dark),
                        selected = darkThemeConfig == DarkThemeConfig.DARK,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
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
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}