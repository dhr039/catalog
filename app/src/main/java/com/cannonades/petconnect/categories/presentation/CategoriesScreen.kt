package com.cannonades.petconnect.categories.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.model.UICategory


@Composable
fun CategoriesRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    CategoriesScreen(viewState = viewState, onEvent = { viewModel.onEvent(it) }, modifier = modifier)

}

@Composable
fun CategoriesScreen(
    viewState: CategoriesViewState,
    onEvent: (CategoriesEvent) -> Unit,
    modifier: Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(CategoriesEvent.RequestMoreCategories)
    }

    CategoriesList(categories = viewState.categories)
}

@Composable
fun CategoriesList(categories: List<UICategory>) {
    Column {
        categories.forEach { category ->
            Text(category.name)
        }
    }
}