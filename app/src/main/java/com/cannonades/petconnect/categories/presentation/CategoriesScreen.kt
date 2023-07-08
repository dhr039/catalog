package com.cannonades.petconnect.categories.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.model.UICategory


@Composable
fun CategoriesRoute(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel(),
    openCategoryScreen: (String) -> Unit
) {
    val viewState by viewModel.state.collectAsState()

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
    viewState: CategoriesViewState,
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

@Composable
fun CategoriesList(
    modifier: Modifier,
    categories: List<UICategory>,
    openCategoryScreen: (String) -> Unit
) {
    Column {
        categories.forEach { category ->
            Card(
                modifier = modifier
                    .padding(20.dp)
                    .clickable { openCategoryScreen(category.id) }) {
                Text(category.name)
            }
        }
    }
}