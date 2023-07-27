package com.cannonades.petconnect.feature.categories.presentation

sealed class CategoriesEvent {
    object RequestMoreCategories: CategoriesEvent()
}