package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.presentation.ui.AnimalScreen
import com.cannonades.petconnect.common.presentation.ui.theme.JetRedditThemeSettings
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import com.cannonades.petconnect.feature.breeds.presentation.AnimalsOfBreedRoute
import com.cannonades.petconnect.feature.breeds.presentation.BreedCategoriesRoute
import com.cannonades.petconnect.feature.categories.presentation.AnimalsOfCategoryRoute
import com.cannonades.petconnect.feature.categories.presentation.CategoriesRoute
import com.cannonades.petconnect.feature.home.presentation.HomeRoute
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

    private lateinit var billingClient: BillingClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        billingClient = BillingClient.newBuilder(this@MainActivity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.e("MainActivity", "onBillingSetupFinished")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    Log.e("MainActivity", "BillingResponseCode.OK")
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.e("MainActivity", "onBillingServiceDisconnected")
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })

        setContent {
            AppContent(
                settingsViewModel,
                networkViewModel.networkStatus.collectAsState(),
                billingClient = billingClient
            )
        }
    }

    // Make sure to end the service connection to prevent memory leaks
    override fun onDestroy() {
        super.onDestroy()
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }

    val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        Log.v("MainActivity", "PurchasesUpdatedListener")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.e("MainActivity", "error user cancelled")
        } else {
            // Handle any other error codes.
            Log.e("MainActivity", "some other error")
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            // Grant entitlement to the user, then consume the purchase.
//            grantCoffeeToUser()
            lifecycleScope.launch {
                settingsViewModel.notifyPurchaseCompleted()
            }
            val consumeParams =
                ConsumeParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)
                    .build()
            billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // Handle the success of the consume operation.
                }
            }
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(
    settingsViewModel: SettingsViewModel,
    networkStatus: State<Boolean>,
    billingClient: BillingClient
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

        var isSnackbarVisible by rememberSaveable { mutableStateOf(false) }
        var snackbarMessage by rememberSaveable { mutableStateOf("") }

        fun showSnackbar(message: String) {
            snackbarMessage = message
            isSnackbarVisible = true
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

        LaunchedEffect(settingsViewModel) {
            settingsViewModel.purchaseEvent.collect { eventMessage ->
                showSnackbar(eventMessage)
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
                darkThemeConfig = darkThemeConfig,
                billingClient = billingClient
            )
        }

        Box(Modifier.fillMaxSize()) {
            Scaffold(
                snackbarHost = {
                    if (isSnackbarVisible) {
                        Snackbar(
                            action = {
                                TextButton(onClick = { isSnackbarVisible = false }) {
                                    Text(stringResource(R.string.dismiss_dialog))
                                }
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(snackbarMessage)
                        }

                        LaunchedEffect(isSnackbarVisible) {
                            delay(4000L)
                            isSnackbarVisible = false
                        }
                    }
                },
                bottomBar = {
                    PetConnectBottomNavigation(
                        currentScreen = currentScreen,
                        onNavigate = { route ->
                            navController.navigateSingleTopTo(route)
                        },
                        showSettingsDialog = showSettingsDialog,
                        onToggleSettingsDialog = { show ->
                            showSettingsDialog = show
                        }
                    )
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
fun PetConnectBottomNavigation(
    currentScreen: PetConnectDestination,
    onNavigate: (route: String) -> Unit,
    showSettingsDialog: Boolean,
    onToggleSettingsDialog: (Boolean) -> Unit
) {
    BottomAppBar(actions = {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.cat_house),
                    contentDescription = Home.route,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(id = R.string.home)) },
            selected = currentScreen == Home,
            onClick = { onNavigate(Home.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.tail_up_cat),
                    contentDescription = Categories.route,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(id = R.string.categories)) },
            selected = currentScreen == Categories || currentScreen == AnimalsOfCategory,
            onClick = { onNavigate(Categories.route) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.cat_head),
                    contentDescription = Breeds.route,
                    modifier = Modifier.size(24.dp)
                )
            },
            label = { Text(stringResource(id = R.string.breeds)) },
            selected = currentScreen == Breeds || currentScreen == AnimalsOfBreed,
            onClick = { onNavigate(Breeds.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "settings") },
            label = { Text(stringResource(id = R.string.settings)) },
            selected = showSettingsDialog,
            onClick = { onToggleSettingsDialog(true) }
        )
    })
}

@Preview(showBackground = true)
@Composable
fun PetConnectBottomNavigationPreview() {
    PetConnectTheme {
        PetConnectBottomNavigation(
            currentScreen = Home,
            onNavigate = {},
            onToggleSettingsDialog = {},
            showSettingsDialog = false
        )
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
            BreedCategoriesRoute(openCategoryScreen = { categ ->
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