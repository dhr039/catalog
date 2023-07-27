package com.cannonades.petconnect.common.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.NoMoreCategoriesException
import com.cannonades.petconnect.common.presentation.ui.AnimalsListViewState

@Composable
fun AnimalsOfCategoryGenericScreen(
    modifier: Modifier,
    viewState: AnimalsListViewState,
    showSnackbar: (String) -> Unit,
    categId: String?,
    onRequestMoreWithSpecificCategory: (String) -> Unit,
    onAnimalClick: (String) -> Unit
) {
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
                .padding(8.dp),
            text = viewState.categName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic)
        )

        if (!categId.isNullOrEmpty()) {
            LaunchedEffect(Unit) {
                onRequestMoreWithSpecificCategory(categId)
            }

            AnimalGrid(modifier, viewState, onEndOfList = {
                onRequestMoreWithSpecificCategory(categId)
            }, onAnimalClick = onAnimalClick)
        }
    }
}