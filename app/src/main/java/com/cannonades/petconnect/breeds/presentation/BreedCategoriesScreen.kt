package com.cannonades.petconnect.breeds.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.model.UICategory
import com.cannonades.petconnect.common.presentation.ui.components.BreedCategoriesList
import com.cannonades.petconnect.common.presentation.ui.components.CategoryCard


@Composable
fun BreedCategoriesRoute(
    modifier: Modifier = Modifier,
    viewModel: BreedCategoriesViewModel = hiltViewModel(),
    openCategoryScreen: (String) -> Unit
) {
    val viewState by viewModel.breedsCategoriesUiStateStateFlow.collectAsState()

    BreedCategoriesScreen(
        viewState = viewState,
        onEvent = { viewModel.onEvent(it) },
        modifier = modifier,
        openCategoryScreen = openCategoryScreen
    )

}

@Composable
fun BreedCategoriesScreen(
    modifier: Modifier,
    viewState: BreedsCategoriesUiState,
    onEvent: (BreedsEvent) -> Unit,
    openCategoryScreen: (String) -> Unit
) {

//    Box(modifier = modifier.padding(horizontal = 16.dp)) {
//        CategoryCard(
//            category = UICategory(id = "dkkdkd", name = "All Breeds", checked = false),
//            onClick = { openCategoryScreen("all_breeds") }
//        )
//    }


    LaunchedEffect(Unit) {
        onEvent(BreedsEvent.RequestMoreBreeds)
    }

    BreedCategoriesList( //TODO: find how to re-use the other CategoriesComponent
        modifier = modifier,
        categories = viewState.breeds,
        openCategoryScreen = openCategoryScreen
    )
}