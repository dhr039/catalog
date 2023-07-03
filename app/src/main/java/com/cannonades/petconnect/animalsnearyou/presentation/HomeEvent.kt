package com.cannonades.petconnect.animalsnearyou.presentation

sealed class HomeEvent {
  object LoadAnimalsIfEmpty: HomeEvent()
  object RequestMoreAnimals: HomeEvent()
}
