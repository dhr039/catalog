package com.cannonades.petconnect.animalsnearyou.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cannonades.petconnect.common.presentation.model.UIAnimal


@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val viewState by viewModel.state.collectAsState()

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
    when {
        viewState.failure != null -> {
            Column {
                Text(text = "Error: ${viewState.failure.getContentIfNotHandled()?.localizedMessage}")
                AnimalGrid(animals = viewState.animals) {}
            }
        }

        else -> {
            onEvent(HomeEvent.LoadAnimalsIfEmpty)
            AnimalGrid(animals = viewState.animals) { onEvent(HomeEvent.RequestMoreAnimals) }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimalGrid(animals: List<UIAnimal>, onEvent: (HomeEvent) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(8.dp),
        content = {
            itemsIndexed(animals) { index, animal ->
                UIAnimalComposable(animal = animal)

                // Detect when we've reached the last item and trigger loading more data
                if (index == animals.lastIndex) {
                    onEvent(HomeEvent.RequestMoreAnimals)
                }
            }
        }
    )
}

@Composable
fun UIAnimalComposable(animal: UIAnimal) {
    Column(modifier = Modifier.padding(16.dp)) {
        val painter = rememberAsyncImagePainter(model = animal.name)

        Card(
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(15.dp),
        ) {
            Image(
                painter = painter,
                contentDescription = "Animal image",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}
