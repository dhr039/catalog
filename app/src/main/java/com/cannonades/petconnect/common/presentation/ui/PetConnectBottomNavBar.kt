package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.AnimalsOfBreed
import com.cannonades.petconnect.common.AnimalsOfCategory
import com.cannonades.petconnect.common.Breeds
import com.cannonades.petconnect.common.Categories
import com.cannonades.petconnect.common.Home
import com.cannonades.petconnect.common.PetConnectDestination
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme

@Composable
fun PetConnectBottomNavBar(
    currentScreen: PetConnectDestination,
    onNavigate: (route: String) -> Unit,
    showSettingsDialog: Boolean,
    onToggleSettingsDialog: (Boolean) -> Unit
) {
    BottomAppBar(actions = {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.cat_house),
                    contentDescription = Home.route,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(id = R.string.home)) },
            selected = currentScreen == Home,
            onClick = { onNavigate(Home.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.tail_up_cat),
                    contentDescription = Categories.route,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(id = R.string.categories)) },
            selected = currentScreen == Categories || currentScreen == AnimalsOfCategory,
            onClick = { onNavigate(Categories.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.cat_head),
                    contentDescription = Breeds.route,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(id = R.string.breeds)) },
            selected = currentScreen == Breeds || currentScreen == AnimalsOfBreed,
            onClick = { onNavigate(Breeds.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "settings") },
            label = { Text(stringResource(id = R.string.settings)) },
            selected = showSettingsDialog,
            onClick = { onToggleSettingsDialog(true) }
        )
    })
}

@Preview(showBackground = true)
@Composable
fun PetConnectBottomNavigationPreview() {
    PetConnectTheme {
        PetConnectBottomNavBar(
            currentScreen = Home,
            onNavigate = {},
            onToggleSettingsDialog = {},
            showSettingsDialog = false
        )
    }
}