package com.cannonades.petconnect.animalsnearyou.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cannonades.petconnect.R
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.presentation.model.UIAnimal
import com.cannonades.petconnect.common.presentation.ui.AnimalsListViewState


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
    viewState: AnimalsListViewState,
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
fun AnimalGrid(viewState: AnimalsListViewState, onEvent: (HomeEvent) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            content = {
                itemsIndexed(viewState.animals) { index, animal ->
                    UIAnimalComposable(animal = animal)
                    /* detect when we've reached the last item and trigger loading more data */
                    if (index == viewState.animals.lastIndex && !viewState.loading) {
                        LaunchedEffect(index) {
                            onEvent(HomeEvent.RequestMoreAnimals)
                        }
                    }
                }
            }
        )
        if (viewState.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun UIAnimalComposable(animal: UIAnimal, modifier: Modifier = Modifier) {
    Box(Modifier.padding(4.dp)) {
        AsyncImage(
            model = animal.name,
            contentDescription = "Animal image",
            placeholder = painterResource(R.drawable.ic_jetnews_logo),
            modifier = modifier.size(200.dp)
        )
    }
}

@Preview
@Composable
fun AnimalGridPreview() {
    val animals = listOf(
        UIAnimal("1", "Dog", "dog_photo_url"),
        UIAnimal("2", "Cat", "cat_photo_url"),
        UIAnimal("2", "Cat", "cat_photo_url"),
        UIAnimal("2", "Cat", "cat_photo_url"),
        UIAnimal("2", "Cat", "cat_photo_url"),
        UIAnimal("3", "Rabbit", "rabbit_photo_url")
    )
    val homeViewState = AnimalsListViewState(animals = animals, loading = true)
    AnimalGrid(viewState = homeViewState, onEvent = {})
}