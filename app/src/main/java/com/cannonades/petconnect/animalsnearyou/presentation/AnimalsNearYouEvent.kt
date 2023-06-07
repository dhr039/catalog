package com.cannonades.petconnect.animalsnearyou.presentation

sealed class AnimalsNearYouEvent {
  object RequestInitialAnimalsList: AnimalsNearYouEvent()
  object RequestMoreAnimals: AnimalsNearYouEvent()
}
