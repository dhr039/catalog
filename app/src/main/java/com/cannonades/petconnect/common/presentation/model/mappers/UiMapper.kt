package com.cannonades.petconnect.common.presentation.model.mappers

interface UiMapper<E, V> {

  fun mapToView(input: E): V
}
