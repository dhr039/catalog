package com.cannonades.petconnect.breeds.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.NoMoreCategoriesException
import com.cannonades.petconnect.common.presentation.ui.components.AnimalGrid

@Composable
fun AnimalsOfBreedScreen(
    modifier: Modifier = Modifier,
    categId: String?,
    showSnackbar: (String) -> Unit,
    viewModel: AnimalsOfBreedViewModel = hiltViewModel(),
    onAnimalClick: (String) -> Unit
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

    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = categId ?: "nothing",
            textAlign = TextAlign.Center
        )

        if (!categId.isNullOrEmpty()) {
            LaunchedEffect(Unit) {
                viewModel.onRequestMoreWithSpecificBreedEvent(categId)
            }

            AnimalGrid(modifier, viewState, onEndOfList = {
                viewModel.onRequestMoreWithSpecificBreedEvent(categId)
            }, onAnimalClick = onAnimalClick)
        }
    }

}