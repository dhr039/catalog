package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouEvent
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouFragmentViewModel
import com.cannonades.petconnect.animalsnearyou.presentation.HomeScreen
import com.cannonades.petconnect.breeds.presentation.BreedsScreen
import com.cannonades.petconnect.common.presentation.ui.components.PetConnectTopAppBar
import com.cannonades.petconnect.common.presentation.ui.theme.JetRedditThemeSettings
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import com.cannonades.petconnect.settings.SettingsDialog
import com.cannonades.petconnect.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AnimalsNearYouFragmentViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onEvent(AnimalsNearYouEvent.RequestInitialAnimalsList)

        setContent {
            AppContent(viewModel, settingsViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(viewModel: AnimalsNearYouFragmentViewModel, settingsViewModel: SettingsViewModel) {
    PetConnectTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            tabRowScreens.find { it.route == currentDestination?.route } ?: Home

        var showSettingsDialog by rememberSaveable {
            mutableStateOf(false)
        }

        val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState(false)
        LaunchedEffect(isDarkTheme) {
            JetRedditThemeSettings.isInDarkTheme.value = isDarkTheme
        }
        val coroutineScope = rememberCoroutineScope()

        if (showSettingsDialog) {
            SettingsDialog(
                onDismiss = { showSettingsDialog = false },
                onThemeChange = {
                    coroutineScope.launch {
                        settingsViewModel.updateDarkThemeSetting(!isDarkTheme)
                    }
                }
            )
        }

        Scaffold(
            topBar = {
                PetConnectTopAppBar(
                    titleRes = currentScreen.titleRes,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = null,
                    actionIcon = Icons.Filled.Settings,
                    actionIconContentDescription = null,
                    onActionClick = { showSettingsDialog = true },
                )
            },
            bottomBar = {
                NavigationBar() {
                    NavigationBarItem(
                        icon = { Icon(Home.icon, contentDescription = Home.route) },
                        label = { Text("Home") },
                        selected = currentScreen == Home,
                        onClick = { navController.navigateSingleTopTo(Home.route) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Breeds.icon, contentDescription = Breeds.route) },
                        label = { Text("Breeds") },
                        selected = currentScreen == Breeds,
                        onClick = { navController.navigateSingleTopTo(Breeds.route) }
                    )
                }
            },
            content = { innerPadding ->
                PetConnectNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        )
    }
}

@Composable
fun PetConnectNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeScreen()
        }
        composable(route = Breeds.route) {
            BreedsScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }