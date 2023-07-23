package com.cannonades.petconnect.common.presentation.ui

import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UIAnimal

data class AnimalViewState(
    val loading: Boolean = false,
    val animal: UIAnimal? = null,
    val failure: Event<Throwable>? = null,
)