package com.cannonades.petconnect.breeds.presentation

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
fun BreedsRoute(
    modifier: Modifier = Modifier,
    viewModel: BreedsViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    BreedsScreen(viewState = viewState, onEvent = { viewModel.onEvent(it) }, modifier = modifier)

}

@Composable
fun BreedsScreen(
    viewState: BreedsViewState,
    onEvent: (BreedsEvent) -> Unit,
    modifier: Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(BreedsEvent.RequestMoreCategories)
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