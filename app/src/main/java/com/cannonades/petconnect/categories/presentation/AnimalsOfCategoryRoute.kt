package com.cannonades.petconnect.categories.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.ui.components.AnimalsOfCategoryGenericScreen

@Composable
fun AnimalsOfCategoryRoute(
    modifier: Modifier = Modifier,
    categId: String?,
    showSnackbar: (String) -> Unit,
    viewModel: AnimalsOfCategoryViewModel = hiltViewModel(),
    onAnimalClick: (String) -> Unit
) {
    val viewState by viewModel.state.collectAsState()


    AnimalsOfCategoryGenericScreen(
        viewState = viewState,
        showSnackbar = showSnackbar,
        categId = categId,
        onRequestMoreWithSpecificCategory = { viewModel.onRequestMoreWithSpecificCategoryEvent(it) },
        modifier = modifier,
        onAnimalClick = onAnimalClick
    )

}