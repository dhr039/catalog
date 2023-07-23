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
import androidx.compose.ui.platform.LocalConfiguration
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

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val dragMultiplier = 3f  // Increase this to make image move faster

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = maxOf(1f, scale * zoomChange)

        // Calculate the available offset, considering the scale
        val maxOffsetX = maxOf(0f, ((imageWidth * scale) - screenWidth) / 2)
        val maxOffsetY = maxOf(0f, ((imageHeight * scale) - screenHeight) / 2)

        if (scale > 1f) {
            offset += offsetChange * dragMultiplier

            // Prevent image from moving off screen
            offset = Offset(
                x = offset.x.coerceIn(-maxOffsetX, maxOffsetX),
                y = offset.y.coerceIn(-maxOffsetY, maxOffsetY)
            )
        } else {
            // Reset offset when image is not zoomed in
            offset = Offset.Zero
        }
    }

    animalData?.photo?.url?.let { url ->
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state)
                .aspectRatio(imageWidth / imageHeight),
            contentScale = ContentScale.Crop
        )
    }
}