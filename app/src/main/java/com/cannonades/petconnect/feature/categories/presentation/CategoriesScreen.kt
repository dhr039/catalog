package com.cannonades.petconnect.feature.categories.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.ui.components.CategoriesList


@Composable
fun CategoriesRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel(),
    openCategoryScreen: (String) -> Unit
) {
    val viewState by viewModel.categoriesUiState.collectAsState()

    CategoriesScreen(
        viewState = viewState,
        onEvent = { viewModel.onEvent(it) },
        modifier = modifier,
        openCategoryScreen = openCategoryScreen
    )

}

@Composable
fun CategoriesScreen(
    modifier: Modifier,
    viewState: CategoriesUiState,
    onEvent: (CategoriesEvent) -> Unit,
    openCategoryScreen: (String) -> Unit
) {
    LaunchedEffect(Unit) {
        onEvent(CategoriesEvent.RequestMoreCategories)
    }

    CategoriesList(
        modifier = modifier,
        categories = viewState.categories,
        openCategoryScreen = openCategoryScreen
    )
}


