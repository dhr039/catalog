package com.cannonades.petconnect.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

interface PetConnectDestination {
    val icon: ImageVector
    val route: String
}

object Home : PetConnectDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
}

object Breeds : PetConnectDestination {
    override val icon = Icons.Filled.List
    override val route = "breeds"
}

object Favorites : PetConnectDestination {
    override val icon = Icons.Filled.Favorite
    override val route = "favorites"
}

val tabRowScreens = listOf(Home, Breeds)