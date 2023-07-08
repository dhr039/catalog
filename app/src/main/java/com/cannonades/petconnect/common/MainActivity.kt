package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cannonades.petconnect.R
import com.cannonades.petconnect.animalsnearyou.presentation.HomeRoute
import com.cannonades.petconnect.categories.presentation.AnimalsOfCategoryScreen
import com.cannonades.petconnect.categories.presentation.CategoriesRoute
import com.cannonades.petconnect.common.presentation.ui.components.NoInternetWarning
import com.cannonades.petconnect.common.presentation.ui.components.PetConnectTopAppBar
import com.cannonades.petconnect.common.presentation.ui.theme.JetRedditThemeSettings
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import com.cannonades.petconnect.settings.presentation.SettingsDialog
import com.cannonades.petconnect.settings.presentation.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()
    private val networkViewModel: NetworkViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppContent(
                settingsViewModel,
                networkViewModel.networkStatus.collectAsState()
            )
        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(
    settingsViewModel: SettingsViewModel,
    networkStatus: State<Boolean>
) {
    PetConnectTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen =
            tabRowScreens.find { it.route == currentDestination?.route } ?: Home

        var showSettingsDialog by rememberSaveable {
            mutableStateOf(false)
        }

        var showSnackbar by remember { mutableStateOf(false) }
        var snackbarMessage by remember { mutableStateOf("") }

        fun showSnackbar(message: String) {
            snackbarMessage = message
            showSnackbar = true
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

        Box(Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = {
                    if (showSnackbar) {
                        Snackbar(
                            action = {
                                TextButton(onClick = { showSnackbar = false }) {
                                    Text("Dismiss")
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(snackbarMessage)
                        }
                    }
                },
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
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Home.icon, contentDescription = Home.route) },
                            label = { Text("Home") },
                            selected = currentScreen == Home,
                            onClick = { navController.navigateSingleTopTo(Home.route) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Categories.icon, contentDescription = Categories.route) },
                            label = { Text(stringResource(id = R.string.categories)) },
                            selected = currentScreen == Categories,
                            onClick = { navController.navigateSingleTopTo(Categories.route) }
                        )
                    }
                },
                content = { innerPadding ->
                    PetConnectNavHost(
                        navController = navController,
                        showSnackbar = ::showSnackbar,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            )

            if (!networkStatus.value) {
                NoInternetWarning()
            }
        }

    }
}

@Composable
fun PetConnectNavHost(
    navController: NavHostController,
    showSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier
    ) {
        composable(route = Home.route) {
            HomeRoute(showSnackbar = showSnackbar)
        }
        composable(route = Categories.route) {
            CategoriesRoute(openCategoryScreen = { categ ->
                navController.navigateToAnimalsCategoriesList(categ)
            })
        }
        composable(
            route = AnimalsOfCategory.routeWithArgs,
            arguments = AnimalsOfCategory.arguments,
        ) { navBackStackEntry ->
            val accountType =
                navBackStackEntry.arguments?.getString(AnimalsOfCategory.categTypeArg)
            AnimalsOfCategoryScreen(categId = accountType)
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

private fun NavHostController.navigateToAnimalsCategoriesList(category: String) {
    this.navigate("${AnimalsOfCategory.route}/$category")
}