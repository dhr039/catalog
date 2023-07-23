package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

/**
 * Multitouch: Panning, zooming, rotating:
 * https://developer.android.com/jetpack/compose/touch-input/pointer-input/multi-touch
 * */
@Composable
fun AnimalScreen(animalId: String, viewModel: AnimalViewModel = hiltViewModel()) {
    LaunchedEffect(animalId) {
        viewModel.loadAnimal(animalId)
    }

    val animalData by viewModel.animal.collectAsState()

    val imageWidth = animalData?.photo?.width?.toFloat() ?: 1f
    val imageHeight = animalData?.photo?.height?.toFloat() ?: 1f

    var scale by remember { mutableStateOf(1f) }
    val state = rememberTransformableState { zoomChange, _, _ ->
        scale = maxOf(1f, scale * zoomChange)
    }

    animalData?.photo?.url?.let { url ->
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
                .transformable(state = state)
                .aspectRatio(imageWidth / imageHeight),
            contentScale = ContentScale.Crop
        )
    }
}