package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
fun PetConnectNavRail(
    modifier: Modifier = Modifier,
    currentScreen: PetConnectDestination,
    onNavigate: (route: String) -> Unit,
    showSettingsDialog: Boolean,
    onToggleSettingsDialog: (Boolean) -> Unit
) {
    NavigationRail(
        modifier = modifier.padding(start = 8.dp, end = 8.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavigationRailItem(
                selected = currentScreen == Home,
                onClick = { onNavigate(Home.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.cat_house),
                        contentDescription = Home.route,
                        modifier = modifier.size(24.dp)
                    )
                }
            )
            NavigationRailItem(
                selected = currentScreen == Categories || currentScreen == AnimalsOfCategory,
                onClick = { onNavigate(Categories.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.tail_up_cat),
                        contentDescription = Categories.route,
                        modifier = modifier.size(24.dp)
                    )
                }
            )
            NavigationRailItem(
                selected = currentScreen == Breeds || currentScreen == AnimalsOfBreed,
                onClick = { onNavigate(Breeds.route) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.cat_head),
                        contentDescription = Breeds.route,
                        modifier = modifier.size(24.dp)
                    )
                }
            )
            NavigationRailItem(
                selected = showSettingsDialog,
                onClick = { onToggleSettingsDialog(true) },
                icon = {
                    Icon(Icons.Filled.Settings, contentDescription = "settings")
                }
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetConnectNavRailPreview() {
    PetConnectTheme {
        PetConnectNavRail(
            currentScreen = Home,
            onNavigate = {},
            onToggleSettingsDialog = {},
            showSettingsDialog = false
        )
    }
}