package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cannonades.petconnect.common.presentation.ui.components.ZoomableDraggableImage


@Composable
fun AnimalScreen(animalId: String, viewModel: AnimalViewModel = hiltViewModel()) {
    LaunchedEffect(animalId) {
        viewModel.loadAnimal(animalId)
    }

    val animalData by viewModel.animal.collectAsState()

    val imageWidth = animalData?.photo?.width?.toFloat() ?: 1f
    val imageHeight = animalData?.photo?.height?.toFloat() ?: 1f

    animalData?.photo?.url?.let { url ->
        ZoomableDraggableImage(
            imageUrl = url,
            contentDescription = null,
            imageWidth = imageWidth,
            imageHeight = imageHeight,
            imageComponent = { imageUrl, contentDescription, modifier ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = contentDescription,
                    modifier = modifier,
                    contentScale = ContentScale.Crop
                )
            }
        )
    }
}