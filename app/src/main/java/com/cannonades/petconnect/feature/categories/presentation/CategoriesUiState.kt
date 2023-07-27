package com.cannonades.petconnect.feature.categories.presentation

import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UICategory

data class CategoriesUiState(
    val loading: Boolean = false,
    val categories: List<UICategory> = emptyList(),
    val failure: Event<Throwable>? = null,
    val failureHasBeenHandled: Boolean = false
)