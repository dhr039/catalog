package com.cannonades.petconnect.common.presentation.ui

import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UIAnimal


data class AnimalsListViewState(
    val loading: Boolean = false,
    val animals: List<UIAnimal> = emptyList(),
    val failure: Event<Throwable>? = null,
)
