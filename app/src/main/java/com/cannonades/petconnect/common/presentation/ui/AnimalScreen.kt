package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AnimalScreen(animalId: String, viewModel: AnimalViewModel = hiltViewModel()) {
    LaunchedEffect(key1 = animalId) {
        viewModel.loadAnimal(animalId)
    }

    val animalData by viewModel.animal.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "ID: ${animalData?.id ?: "Loading..."}")
        Text(text = "photo object: ${animalData?.photo ?: "Loading..."}")
        Text(text = "Photo URL: ${animalData?.photo?.url ?: "Loading..."}")
    }
}