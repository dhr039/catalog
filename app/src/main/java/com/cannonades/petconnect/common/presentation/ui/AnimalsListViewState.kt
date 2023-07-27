package com.cannonades.petconnect.common.presentation.ui

import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UIAnimal

/**
 * Even if there is an Error or a Loading state we need to have the list of UIAnimal to display
 * since there is an endless scroll. That's why I chose to use a data class rather than sealed classes.
 */
data class AnimalsListViewState(
    val categName: String = "",
    val loading: Boolean = false,
    val animals: List<UIAnimal> = emptyList(),
    val failure: Event<Throwable>? = null,
)
