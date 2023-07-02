package com.cannonades.petconnect.animalsnearyou.presentation

sealed class HomeEvent {
  object RequestInitialAnimalsList: HomeEvent()
  object RequestMoreAnimals: HomeEvent()
}
