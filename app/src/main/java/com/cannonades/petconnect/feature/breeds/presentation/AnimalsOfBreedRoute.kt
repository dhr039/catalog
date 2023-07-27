package com.cannonades.petconnect.feature.breeds.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.common.presentation.ui.components.AnimalsOfCategoryGenericScreen

@Composable
fun AnimalsOfBreedRoute(
    modifier: Modifier = Modifier,
    categId: String?,
    showSnackbar: (String) -> Unit,
    viewModel: AnimalsOfBreedViewModel = hiltViewModel(),
    onAnimalClick: (String) -> Unit
) {
    val viewState by viewModel.state.collectAsState()

    AnimalsOfCategoryGenericScreen(
        modifier = modifier,
        viewState = viewState,
        showSnackbar = showSnackbar,
        categId = categId,
        onRequestMoreWithSpecificCategory = { viewModel.onRequestMoreWithSpecificBreedEvent(it) },
        onAnimalClick = onAnimalClick
    )
}