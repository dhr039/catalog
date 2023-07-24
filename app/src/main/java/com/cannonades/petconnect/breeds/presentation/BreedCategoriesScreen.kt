package com.cannonades.petconnect.breeds.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.categories.presentation.CategoriesEvent
import com.cannonades.petconnect.categories.presentation.CategoriesUiState
import com.cannonades.petconnect.categories.presentation.CategoriesViewModel
import com.cannonades.petconnect.common.presentation.ui.components.CategoriesList


@Composable
fun BreedCategoriesRoute(
    modifier: Modifier = Modifier,
    viewModel: BreedCategoriesViewModel = hiltViewModel(),
    openCategoryScreen: (String) -> Unit
) {
//    val viewState by viewModel.categoriesUiState.collectAsState()
//
//    BreedCategoriesScreen(
//        viewState = viewState,
//        onEvent = { viewModel.onEvent(it) },
//        modifier = modifier,
//        openCategoryScreen = openCategoryScreen
//    )

}

@Composable
fun BreedCategoriesScreen(
    modifier: Modifier,
    viewState: CategoriesUiState,
    onEvent: (CategoriesEvent) -> Unit,
    openCategoryScreen: (String) -> Unit
) {
//    LaunchedEffect(Unit) {
//        onEvent(CategoriesEvent.RequestMoreCategories)
//    }
//
//    CategoriesList(
//        modifier = modifier,
//        categories = viewState.categories,
//        openCategoryScreen = openCategoryScreen
//    )
}