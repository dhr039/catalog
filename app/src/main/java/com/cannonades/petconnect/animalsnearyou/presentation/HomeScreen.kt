package com.cannonades.petconnect.animalsnearyou.presentation

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
fun HomeRoute(
    modifier: Modifier = Modifier,
    showSnackbar: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()
    val context = LocalContext.current
    val isInitialListLoaded by viewModel.isInitialListLoaded.collectAsState()

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

    LaunchedEffect(isInitialListLoaded) {
        if (isInitialListLoaded) {
            viewModel.onEvent(HomeEvent.LoadAnimalsIfEmpty)
        }
    }

    AnimalGrid(modifier, viewState, { viewModel.onEvent(it) })
}