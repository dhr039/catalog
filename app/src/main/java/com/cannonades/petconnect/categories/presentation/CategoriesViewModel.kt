package com.cannonades.petconnect.categories.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.categories.domain.GetCategoriesFromCacheUseCase
import com.cannonades.petconnect.categories.domain.RequestCategoriesUseCase
import com.cannonades.petconnect.common.presentation.model.UICategory
import com.cannonades.petconnect.common.presentation.model.mappers.UICategoryMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val requestCategories: RequestCategoriesUseCase,
    private val getCategoriesFromCache: GetCategoriesFromCacheUseCase,
    private val uiCategoryMapper: UICategoryMapper
) : ViewModel() {

    init {
        viewModelScope.launch {
            getCategoriesFromCache().collect { categories ->
                onNewCategoriesList(categories.map { category -> uiCategoryMapper.mapToView(category) })
            }
        }
    }

    private val _state = MutableStateFlow(CategoriesViewState())
    val state: StateFlow<CategoriesViewState> = _state.asStateFlow()

    private fun onNewCategoriesList(categories: List<UICategory>) {
        if (categories.isNotEmpty()) {
            Log.e("DHR", "onNewCategoriesList ${categories.size}")
            val updatedCategoriesSet = (state.value.categories + categories).toSet()
            _state.update { oldState ->
                oldState.copy(categories = updatedCategoriesSet.toList())
            }
        }
    }

    fun onEvent(event: CategoriesEvent) {
        when (event) {
            is CategoriesEvent.RequestMoreCategories -> loadNextCategoriesPage()
        }
    }

    private fun loadNextCategoriesPage() {
        viewModelScope.launch {
            requestCategories()
        }
    }

    fun updateCategory(category: UICategory) {
        viewModelScope.launch {
            //TODO: save to db, remember that saving should be finished even if we exit viewModelScope
            Log.e("DHR", "updateCategory ${category.toString()}")
        }
    }

    fun refreshCategories() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                requestCategories()
            } finally {
                _state.update { it.copy(loading = false) }
            }
        }
    }
}