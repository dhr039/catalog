package com.cannonades.petconnect.common.presentation.ui

import com.cannonades.petconnect.R

sealed class Screen(val titleResId: Int, val route: String) {
    object Home : Screen(R.string.home, "Home")
    object Breeds : Screen(R.string.breeds, "Breeds")

    companion object {
        fun fromRoute(route: String?): Screen {
            return when (route) {
                Home.route -> Home
                Breeds.route -> Breeds
                else -> Home
            }
        }
    }
}