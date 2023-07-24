package com.cannonades.petconnect.breeds.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.model.UICategory
import com.cannonades.petconnect.common.presentation.ui.components.CategoryCard


@Composable
fun BreedCategoriesRoute(
    modifier: Modifier = Modifier,
    viewModel: BreedCategoriesViewModel = hiltViewModel(),
    openCategoryScreen: (String) -> Unit
) {
//    val viewState by viewModel.categoriesUiState.collectAsState()
//
    BreedCategoriesScreen(
//        viewState = viewState,
//        onEvent = { viewModel.onEvent(it) },
        modifier = modifier,
        openCategoryScreen = openCategoryScreen
    )

}

@Composable
fun BreedCategoriesScreen(
    modifier: Modifier,
//    viewState: CategoriesUiState,
//    onEvent: (CategoriesEvent) -> Unit,
    openCategoryScreen: (String) -> Unit
) {

//    LazyColumn(
//        modifier = modifier.padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        items(categories.size) { index ->
//            val category = categories[index]
//            CategoryCard(
//                category = category,
//                onClick = { openCategoryScreen(category.id) }
//            )
//        }
//    }

    Box(modifier = modifier.padding(horizontal = 16.dp)) {
        CategoryCard(
            category = UICategory(id = "dkkdkd", name = "All Breeds", checked = false),
            onClick = { openCategoryScreen("all_breeds") }
        )
    }


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