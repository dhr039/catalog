package com.cannonades.petconnect.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.cannonades.petconnect.R

interface PetConnectDestination {
    val icon: ImageVector
    val route: String
    val titleRes: Int
}

object Home : PetConnectDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
    override val titleRes = R.string.home
}

object Categories : PetConnectDestination {
    override val icon = Icons.Filled.List
    override val route = "categories"
    override val titleRes = R.string.categories
}

object Favorites : PetConnectDestination {
    override val icon = Icons.Filled.Favorite
    override val route = "favorites"
    override val titleRes = R.string.favorites
}

val tabRowScreens = listOf(Home, Categories)