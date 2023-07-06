package com.cannonades.petconnect.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cannonades.petconnect.R
import com.cannonades.petconnect.animalsnearyou.presentation.HomeRoute
import com.cannonades.petconnect.animalsnearyou.presentation.HomeViewModel
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

    private val viewModel: HomeViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    // Mutable state to store network status
    var networkStatus = mutableStateOf(true)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            networkStatus.value = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            networkStatus.value = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkStatus.value = checkNetworkAvailability()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        setContent {
            AppContent(viewModel, settingsViewModel, networkStatus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    // check initial network availability
    private fun checkNetworkAvailability(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(
    viewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    networkStatus: MutableState<Boolean>
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

            // Network status warning
            if (!networkStatus.value) {
                NoInternetWarning()
            }
        }


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
            HomeRoute()
        }
        composable(route = Breeds.route) {
            BreedsScreen()
        }
    }
}

@Composable
fun NoInternetWarning() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_internet_connection),
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall.copy(fontStyle = FontStyle.Italic)
        )
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