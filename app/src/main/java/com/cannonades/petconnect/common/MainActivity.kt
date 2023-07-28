package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
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
import com.cannonades.petconnect.feature.home.presentation.HomeRoute
import com.cannonades.petconnect.feature.breeds.presentation.AnimalsOfBreedRoute
import com.cannonades.petconnect.feature.breeds.presentation.BreedCategoriesRoute
import com.cannonades.petconnect.feature.categories.presentation.AnimalsOfCategoryRoute
import com.cannonades.petconnect.feature.categories.presentation.CategoriesRoute
import com.cannonades.petconnect.common.presentation.ui.AnimalScreen
import com.cannonades.petconnect.common.presentation.ui.theme.JetRedditThemeSettings
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import com.cannonades.petconnect.feature.settings.presentation.DarkThemeConfig
import com.cannonades.petconnect.feature.settings.presentation.SettingsDialog
import com.cannonades.petconnect.feature.settings.presentation.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

        /**
         * This would work as long as the base routes are unique.
         * But note that it's a bit of a hack, and could lead to unexpected behavior
         * if navigation becomes more complex.
         * */
        val currentScreen = tabRowScreens.find {
            it.route == currentDestination?.route?.substringBefore("/")
        } ?: Home

        var showSettingsDialog by rememberSaveable {
            mutableStateOf(false)
        }

        var showSnackbar by rememberSaveable { mutableStateOf(false) }
        var snackbarMessage by rememberSaveable { mutableStateOf("") }

        fun showSnackbar(message: String) {
            snackbarMessage = message
            showSnackbar = true
        }

        val darkThemeConfig by settingsViewModel.darkThemeConfig.collectAsState(DarkThemeConfig.FOLLOW_SYSTEM)
        val systemDarkTheme = isSystemInDarkTheme()
        LaunchedEffect(systemDarkTheme, darkThemeConfig) {
            JetRedditThemeSettings.currentThemeMode.value = when (darkThemeConfig) {
                DarkThemeConfig.FOLLOW_SYSTEM -> if (systemDarkTheme) DarkThemeConfig.DARK else DarkThemeConfig.LIGHT
                DarkThemeConfig.DARK -> DarkThemeConfig.DARK
                DarkThemeConfig.LIGHT -> DarkThemeConfig.LIGHT
            }
        }

        val coroutineScope = rememberCoroutineScope()
        if (showSettingsDialog) {
            SettingsDialog(
                onDismiss = { showSettingsDialog = false },
                onChangeDarkThemeConfig = { newConfig ->
                    coroutineScope.launch {
                        settingsViewModel.updateDarkThemeConfig(newConfig)
                    }
                },
                darkThemeConfig = darkThemeConfig
            )
        }

        Box(Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = {
                    if (showSnackbar) {
                        Snackbar(
                            action = {
                                TextButton(onClick = { showSnackbar = false }) {
                                    Text(stringResource(R.string.dismiss_dialog))
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(snackbarMessage)
                        }

                        LaunchedEffect(showSnackbar) {
                            delay(3000L)
                            showSnackbar = false
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
                            selected = currentScreen == Categories || currentScreen == AnimalsOfCategory,
                            onClick = { navController.navigateSingleTopTo(Categories.route) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Breeds.icon, contentDescription = Breeds.route) },
                            label = { Text(stringResource(id = R.string.breeds)) },
                            selected = currentScreen == Breeds || currentScreen == AnimalsOfBreed,
                            onClick = { navController.navigateSingleTopTo(Breeds.route) }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Settings, contentDescription = "settings") },
                            label = { Text(stringResource(id = R.string.settings)) },
                            selected = showSettingsDialog == true,
                            onClick = { showSettingsDialog = true }
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
            HomeRoute(
                showSnackbar = showSnackbar,
                onAnimalClick = { navController.navigate("${AnimalDetail.route}/${it}") }
            )
        }
        composable(route = Breeds.route) {
            BreedCategoriesRoute(openCategoryScreen = {categ ->
                navController.navigate("${AnimalsOfBreed.route}/$categ")
            })
        }
        composable(
            route = AnimalsOfBreed.routeWithArgs,
            arguments = AnimalsOfBreed.arguments,
        ) { navBackStackEntry ->
            val categId =
                navBackStackEntry.arguments?.getString(AnimalsOfBreed.categTypeArg)
            AnimalsOfBreedRoute(
                categId = categId,
                showSnackbar = showSnackbar,
                onAnimalClick = { navController.navigate("${AnimalDetail.route}/${it}") }
            )
        }
        composable(route = Categories.route) {
            CategoriesRoute(openCategoryScreen = { categ ->
                navController.navigate("${AnimalsOfCategory.route}/$categ")
            })
        }
        composable(
            route = AnimalsOfCategory.routeWithArgs,
            arguments = AnimalsOfCategory.arguments,
        ) { navBackStackEntry ->
            val categId =
                navBackStackEntry.arguments?.getString(AnimalsOfCategory.categTypeArg)
            AnimalsOfCategoryRoute(
                categId = categId,
                showSnackbar = showSnackbar,
                onAnimalClick = { navController.navigate("${AnimalDetail.route}/${it}") }
            )
        }
        composable(
            AnimalDetail.routeWithArgs,
            arguments = AnimalDetail.arguments
        ) { navBackStackEntry ->
            val animalId = navBackStackEntry.arguments?.getString(AnimalDetail.animalIdArg) ?: ""
            AnimalScreen(animalId = animalId)
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) {
    if (currentDestination?.route != route) {
        popBackStack(graph.startDestinationId, false)
        navigate(route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}