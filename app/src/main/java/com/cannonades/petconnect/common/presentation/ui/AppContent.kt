package com.cannonades.petconnect.common.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cannonades.petconnect.R
import com.cannonades.petconnect.animalsnearyou.presentation.AnimalsNearYouFragmentViewModel
import com.cannonades.petconnect.animalsnearyou.presentation.HomeScreen
import com.cannonades.petconnect.breeds.presentation.BreedsScreen
import com.cannonades.petconnect.common.presentation.ui.theme.PetConnectTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppContent(viewModel: AnimalsNearYouFragmentViewModel) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

//    PetConnectTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            MainScreenContainer(navController = navController, viewModel = viewModel)
//        }
//    }
    Crossfade(targetState = navBackStackEntry?.destination?.route) { route: String? ->

        Scaffold(
            topBar = getTopBar(Screen.fromRoute(route), scaffoldState, coroutineScope),
            drawerContent = {
                AppDrawer(
                    onScreenSelected = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                        coroutineScope.launch { scaffoldState.drawerState.close() }
                    }
                )
            },
            scaffoldState = scaffoldState,
            bottomBar = {
                BottomNavigationComponent(navController = navController)
            },
            content = {
                MainScreenContainer(
                    navController = navController,
                    modifier = Modifier.padding(bottom = 56.dp),
                    viewModel = viewModel
                )
            }
        )
    }

}


fun getTopBar(
    screenState: Screen,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
): @Composable (() -> Unit) {
//    if (screenState == Screen.MyProfile || screenState == Screen.ChooseCommunity) {
//        return {}
//    } else {
        return {
            TopAppBar(
                screen = screenState,
                scaffoldState = scaffoldState,
                coroutineScope = coroutineScope
            )
        }
//    }
}

@Composable
fun TopAppBar(
    screen: Screen,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {

    val colors = androidx.compose.material.MaterialTheme.colors

    androidx.compose.material.TopAppBar(
        title = {
            Text(
                text = stringResource(screen.titleResId),
                color = colors.primaryVariant
            )
        },
        backgroundColor = colors.surface,
        navigationIcon = {
            IconButton(onClick = {
                coroutineScope.launch { scaffoldState.drawerState.open() }
            }) {
                Icon(
                    Icons.Filled.AccountCircle,
                    tint = Color.LightGray,
                    contentDescription = "account"
                )
            }
        }
    )
}

@Composable
private fun BottomNavigationComponent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        NavigationItem(0, R.drawable.ic_baseline_home_24, R.string.home_icon, Screen.Home),
        NavigationItem(
            1,
            R.drawable.ic_baseline_format_list_bulleted_24,
            R.string.subscriptions_icon,
            Screen.Breeds
        ),
//        NavigationItem(2, R.drawable.ic_baseline_add_24, R.string.post_icon, Screen.NewPost),
    )
    BottomNavigation(modifier = modifier) {
        items.forEach {
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it.vectorResourceId),
                        contentDescription = stringResource(id = it.contentDescriptionResourceId)
                    )
                },
                selected = selectedItem == it.index,
                onClick = {
                    selectedItem = it.index
                    navController.navigate(it.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

private data class NavigationItem(
    val index: Int,
    val vectorResourceId: Int,
    val contentDescriptionResourceId: Int,
    val screen: Screen
)


@Composable
private fun MainScreenContainer(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: AnimalsNearYouFragmentViewModel
) {
    androidx.compose.material.Surface(
        modifier = modifier,
        color = androidx.compose.material.MaterialTheme.colors.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
//                HomeScreen(viewModel)
                HomeScreen()
            }
            composable(Screen.Breeds.route) {
                BreedsScreen()
            }
        }
    }
}