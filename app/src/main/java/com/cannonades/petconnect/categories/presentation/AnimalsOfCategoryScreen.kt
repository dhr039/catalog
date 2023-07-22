package com.cannonades.petconnect.categories.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.NoMoreCategoriesException
import com.cannonades.petconnect.common.presentation.ui.components.AnimalGrid

@Composable
fun AnimalsOfCategoryScreen(
    modifier: Modifier = Modifier,
    categId: String?,
    showSnackbar: (String) -> Unit,
    viewModel: AnimalsOfCategoryViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val context = LocalContext.current

    viewState.failure?.getContentIfNotHandled()?.let { failure ->
        val stringId = when (failure) {
            is NetworkException -> R.string.network_error
            is NoMoreAnimalsException -> R.string.no_more_animals_error
            is NoMoreCategoriesException -> R.string.no_more_categories_error
            else -> R.string.unknown_error
        }
        val message = context.getString(stringId)
        showSnackbar(message)
    }



    if(!categId.isNullOrEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.myOnEvent(categId.toInt())
        }

        AnimalGrid(modifier, viewState) {
            viewModel.myOnEvent(categId.toInt())
        }
    }

}