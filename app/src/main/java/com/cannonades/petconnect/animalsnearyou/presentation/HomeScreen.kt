package com.cannonades.petconnect.animalsnearyou.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.presentation.model.UIAnimal


@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    showSnackbar: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

    viewState.failure?.getContentIfNotHandled()?.let { failure ->
        if (failure is NoMoreAnimalsException) {
            showSnackbar("${failure.message}")
            viewModel.handleFailure()
        }
    }

    HomeScreen(
        viewState = viewState,
        onEvent = { viewModel.onEvent(it) },
        modifier = modifier
    )
}


@Composable
fun HomeScreen(
    viewState: HomeViewState,
    onEvent: (HomeEvent) -> Unit,
    modifier: Modifier
) {
    LaunchedEffect(Unit) {
        onEvent(HomeEvent.LoadAnimalsIfEmpty)
    }
    AnimalGrid(
        viewState,
        onEvent = onEvent
    )
}

@Composable
fun AnimalGrid(viewState: HomeViewState, onEvent: (HomeEvent) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(8.dp),
        content = {
            itemsIndexed(viewState.animals) { index, animal ->
                UIAnimalComposable(animal = animal)
                /* detect when we've reached the last item and trigger loading more data */
                if (index == viewState.animals.lastIndex && !viewState.loading && !viewState.failureHasBeenHandled) {
                    LaunchedEffect(index) {
                        onEvent(HomeEvent.RequestMoreAnimals)
                    }
                }
            }
        }
    )
}

@Composable
fun UIAnimalComposable(animal: UIAnimal) {
    Box(Modifier.padding(4.dp)) {
        AsyncImage(
            model = animal.name,
            contentDescription = "Animal image",
            placeholder = painterResource(R.drawable.ic_jetnews_logo),
            modifier = Modifier.size(200.dp)
        )
    }
}
