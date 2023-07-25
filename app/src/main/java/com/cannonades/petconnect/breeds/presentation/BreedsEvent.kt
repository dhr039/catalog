package com.cannonades.petconnect.breeds.presentation

sealed class BreedsEvent {
    object RequestMoreBreeds: BreedsEvent()
}