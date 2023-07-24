package com.cannonades.petconnect.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
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

object Breeds : PetConnectDestination {
    override val icon = Icons.Filled.Favorite
    override val route = "breeds"
    override val titleRes = R.string.breeds
}

object AnimalsOfBreed : PetConnectDestination {
    override val titleRes = R.string.favorites
    override val icon = Icons.Filled.Star

    override val route = "breeds_category"
    const val categTypeArg = "categ_id"
    val routeWithArgs = "$route/{$categTypeArg}"
    val arguments = listOf(
        navArgument(categTypeArg) { type = NavType.StringType }
    )
}

object AnimalsOfCategory : PetConnectDestination {
    override val titleRes = R.string.favorites
    override val icon = Icons.Filled.Star

    override val route = "animals_category"
    const val categTypeArg = "categ_id"
    val routeWithArgs = "$route/{$categTypeArg}"
    val arguments = listOf(
        navArgument(categTypeArg) { type = NavType.StringType }
    )
}

object AnimalDetail : PetConnectDestination {
    override val icon = Icons.Filled.Star
    override val route = "animalDetail"
    override val titleRes = R.string.for_you

    const val animalIdArg = "animal_id"
    val routeWithArgs = "$route/{$animalIdArg}"
    val arguments = listOf(
        navArgument(animalIdArg) { type = NavType.StringType }
    )
}

val tabRowScreens = listOf(Home, Categories, AnimalsOfCategory, Breeds, AnimalsOfBreed)