package com.cannonades.petconnect.common.presentation.ui.components

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetConnectTopAppBar(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    navigationIcon: ImageVector,
    navigationIconContentDescription: String?,
    actionIcon: ImageVector,
    actionIconContentDescription: String?,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    titleTextStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    CenterAlignedTopAppBar(
        modifier = modifier.testTag("niaTopAppBar"),
        title = { Text(text = stringResource(id = titleRes), style = titleTextStyle) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = navigationIcon,
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
//        colors = colors, TODO: check in 'Now in Android' to see if this is needed here
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(name = "Top App Bar")
@Composable
fun PetConnectTopAppBarPreview() {
    PetConnectTopAppBar(
        titleRes = android.R.string.untitled,
        navigationIcon = Icons.Filled.Search,
        navigationIconContentDescription = "Navigation icon",
        actionIcon = Icons.Filled.MoreVert,
        actionIconContentDescription = "Action icon",
    )
}