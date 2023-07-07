package com.cannonades.petconnect.categories.presentation

import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UICategory

data class CategoriesViewState(
    val loading: Boolean = false,
    val categories: List<UICategory> = emptyList(),
//    val noMoreCategories: Boolean = false,
    val failure: Event<Throwable>? = null,
    val failureHasBeenHandled: Boolean = false
)