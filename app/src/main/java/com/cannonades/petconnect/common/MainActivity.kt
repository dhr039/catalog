package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cannonades.petconnect.R
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouEvent
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouFragmentViewModel
import com.cannonades.petconnect.animalsnearyou.presentation.HomeScreen
import com.cannonades.petconnect.breeds.presentation.BreedsScreen
import com.cannonades.petconnect.common.presentation.ui.components.PetConnectTopAppBar
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: AnimalsNearYouFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onEvent(AnimalsNearYouEvent.RequestInitialAnimalsList)

        setContent {
            AppContent(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(viewModel: AnimalsNearYouFragmentViewModel) {
    PetConnectTheme {
//    val coroutineScope: CoroutineScope = rememberCoroutineScope()
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            tabRowScreens.find { it.route == currentDestination?.route } ?: Home


        Scaffold(
            topBar = {
                PetConnectTopAppBar(
                    titleRes = currentScreen.titleRes,
                    navigationIcon = Icons.Filled.Search,
                    navigationIconContentDescription = null,
                    actionIcon = Icons.Filled.Settings,
                    actionIconContentDescription = null
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