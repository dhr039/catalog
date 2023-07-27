package com.cannonades.petconnect.feature.breeds.presentation

sealed class BreedsEvent {
    object RequestMoreBreeds: BreedsEvent()
}