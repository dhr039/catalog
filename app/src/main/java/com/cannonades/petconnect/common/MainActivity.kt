package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
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
import com.cannonades.petconnect.categories.presentation.CategoriesDialog
import com.cannonades.petconnect.categories.presentation.CategoriesRoute
import com.cannonades.petconnect.categories.presentation.CategoriesViewModel
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
    private val categoriesViewModel: CategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppContent(
                settingsViewModel,
                categoriesViewModel,
                networkViewModel.networkStatus.collectAsState()
            )
        }

    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(
    settingsViewModel: SettingsViewModel,
    categoriesViewModel: CategoriesViewModel,
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

        var showCategoriesDialog by rememberSaveable {
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

        if (showCategoriesDialog) {
            CategoriesDialog(
                onDismiss = { showCategoriesDialog = false },
            )
        }

        LaunchedEffect(showCategoriesDialog) {
            if (showCategoriesDialog) {
                categoriesViewModel.loadCategories()
            }
        }

        Box(Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = {
                    if (showSnackbar) {
                        Snackbar(
                            action = {
                                TextButton(onClick = { showSnackbar = false }) {
                                    Text(stringResource(R.string.dismiss))
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(snackbarMessage)
                        }
                    }
                },
                bottomBar = {
                    BottomAppBar(actions = {
                        NavigationBarItem(
                            icon = { Icon(Home.icon, contentDescription = Home.route) },
                            label = { Text(stringResource(id = R.string.home)) },
                            selected = currentScreen == Home,
                            onClick = { navController.navigateSingleTopTo(Home.route) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Categories.icon, contentDescription = Categories.route) },
                            label = { Text(stringResource(id = R.string.categories)) },
                            selected = currentScreen == Categories,
                            onClick = { navController.navigateSingleTopTo(Categories.route) }
                        )
                        if (currentScreen == Home) {
                            IconButton(
                                onClick = { showCategoriesDialog = true },
                                modifier = Modifier,
                                content = { Icon(Icons.Filled.Search, contentDescription = "") },
                            )
                        } else {
                            Box(Modifier.size(48.dp)) //same size as the IconButton (48.dp by default)
                        }
                        IconButton(
                            onClick = { showSettingsDialog = true },
                            content = { Icon(Icons.Filled.Settings, contentDescription = "") },
                        )
                    })


                },
                content = { innerPadding ->
                    PetConnectNavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        showSnackbar = ::showSnackbar,
                    )
                }
            )
            if (!networkStatus.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Text(
                        text = stringResource(R.string.no_internet_connection),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
                    )
                }
            }
        }
    }
}

@Composable
fun PetConnectNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    showSnackbar: (String) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Home.route,
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