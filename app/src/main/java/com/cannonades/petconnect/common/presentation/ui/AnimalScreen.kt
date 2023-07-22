package com.cannonades.petconnect.common.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AnimalScreen(animalId: String, viewModel: AnimalViewModel = hiltViewModel()) {
    // Get the animal data using the ViewModel
//    val animalData by viewModel.getAnimalData(animalId)
//        .collectAsState(initial = UIAnimal("", "", ""))
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        Text(text = "ID: ${animalData.id}")
//        Text(text = "Name: ${animalData.name}")
//        Text(text = "Photo URL: ${animalData.photo}")
//    }



    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "ID: ${animalId}")
    }
}