package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun AnimalScreen(animalId: String, viewModel: AnimalViewModel = hiltViewModel()) {
    LaunchedEffect(animalId) {
        viewModel.loadAnimal(animalId)
    }

    val animalData by viewModel.animal.collectAsState()

    val imageWidth = animalData?.photo?.width?.toFloat() ?: 1f
    val imageHeight = animalData?.photo?.height?.toFloat() ?: 1f

    animalData?.photo?.url?.let { url ->
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(imageWidth / imageHeight),
            contentScale = ContentScale.Crop
        )
    }
    
}