package com.cannonades.petconnect.categories.presentation

sealed class CategoriesEvent {
    object RequestMoreCategories: CategoriesEvent()
}