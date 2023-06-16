package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cannonades.petconnect.animalsnearyou.presentation.HomeScreen
import com.cannonades.petconnect.breeds.presentation.BreedsScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { HomeScreen() }
        composable("breeds") { BreedsScreen() }
    }
}