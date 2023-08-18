package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import com.cannonades.petconnect.common.presentation.ui.PetConnectBottomNavBar
import com.cannonades.petconnect.common.presentation.ui.PetConnectNavRail
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
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
            val windowSizeClass = calculateWindowSizeClass(this)
            AppContent(
                settingsViewModel,
                networkViewModel.networkStatus.collectAsState(),
                billingClient = billingClient,
                windowSize = windowSizeClass
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
    billingClient: BillingClient,
    windowSize: WindowSizeClass
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
            when (windowSize.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    AppContentPortrait(
                        isSnackbarVisible = isSnackbarVisible,
                        onSnackCloseClick = { isSnackbarVisible = false },
                        snackbarMessage = snackbarMessage,
                        bottomBar = {
                            PetConnectBottomNavBar(
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
                        petConnectNavHost = { innerPadding ->
                            PetConnectNavHost(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                showSnackbar = ::showSnackbar
                            )
                        }
                    )
                }

                WindowWidthSizeClass.Expanded, WindowWidthSizeClass.Medium -> {
                    AppContentLandscape(
                        isSnackbarVisible = isSnackbarVisible,
                        onSnackCloseClick = { isSnackbarVisible = false },
                        snackbarMessage = snackbarMessage,
                        navRail = {
                            PetConnectNavRail(
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
                        petConnectNavHost = {
                            PetConnectNavHost(
                                navController = navController,
                                showSnackbar = ::showSnackbar
                            )
                        }
                    )
                }
            }

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
fun AppContentPortrait(
    isSnackbarVisible: Boolean,
    onSnackCloseClick: () -> Unit,
    snackbarMessage: String,
    bottomBar: @Composable () -> Unit,
    petConnectNavHost: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        snackbarHost = {
            if (isSnackbarVisible) {
                Snackbar(
                    action = {
                        TextButton(onClick = onSnackCloseClick) {
                            Text(stringResource(R.string.dismiss_dialog))
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(snackbarMessage)
                }

                LaunchedEffect(isSnackbarVisible) {
                    delay(4000L)
                    onSnackCloseClick()
                }
            }
        },
        bottomBar = { bottomBar() },
        content = { innerPadding -> petConnectNavHost(innerPadding) }
    )
}

@Composable
fun AppContentLandscape(
    isSnackbarVisible: Boolean,
    snackbarMessage: String,
    onSnackCloseClick: () -> Unit,
    navRail: @Composable () -> Unit,
    petConnectNavHost: @Composable () -> Unit,
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box {
            Row {
                navRail()
                petConnectNavHost()
            }
            if (isSnackbarVisible) {
                val snackbarHostState = remember { SnackbarHostState() }
                Snackbar(
                    action = {
                        TextButton(onClick = {
                            onSnackCloseClick()
                        }) {
                            Text(stringResource(R.string.dismiss_dialog))
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(8.dp)
                ) { Text(snackbarMessage) }

                LaunchedEffect(snackbarMessage) {
                    snackbarHostState.showSnackbar(snackbarMessage)
                    onSnackCloseClick()
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