package com.cannonades.petconnect.feature.breeds.presentation

import com.cannonades.petconnect.common.presentation.Event

data class BreedsCategoriesUiState(
    val loading: Boolean = false,
    val breeds: List<UIBreedCategory> = emptyList(),
    val failure: Event<Throwable>? = null,
    val failureHasBeenHandled: Boolean = false
)