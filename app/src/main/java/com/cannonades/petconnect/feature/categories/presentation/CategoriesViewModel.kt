package com.cannonades.petconnect.feature.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UICategory
import com.cannonades.petconnect.common.presentation.model.mappers.UiCategoryMapper
import com.cannonades.petconnect.feature.categories.domain.usecases.GetCategoriesFromCacheUseCase
import com.cannonades.petconnect.feature.categories.domain.usecases.RequestCategoriesUseCase
import com.cannonades.petconnect.feature.categories.domain.usecases.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val requestCategoriesUseCase: RequestCategoriesUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val getCategoriesFromCache: GetCategoriesFromCacheUseCase,
    private val uiCategoryMapper: UiCategoryMapper
) : ViewModel() {

    init {
        viewModelScope.launch {
            getCategoriesFromCache().collect { categories ->
                onNewCategoriesList(categories.map { category -> uiCategoryMapper.mapToView(category) })
            }
        }
    }

    private val _state = MutableStateFlow(CategoriesUiState())
    val categoriesUiState: StateFlow<CategoriesUiState> = _state.asStateFlow()

    private fun onNewCategoriesList(categories: List<UICategory>) {
        if (categories.isNotEmpty()) {
            _state.update { oldState ->
                oldState.copy(categories = categories.toList())
            }
        }
    }

    fun onEvent(event: CategoriesEvent) {
        when (event) {
            is CategoriesEvent.RequestMoreCategories -> loadCategories()
        }
    }

    fun onCategoryChecked(category: UICategory) {
        viewModelScope.launch {
            val updatedCategory = category.copy(checked = !category.checked)
            updateCategoryUseCase(updatedCategory)
        }
    }

    fun loadCategories() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                requestCategoriesUseCase()
            } catch (e: Exception){
                _state.update { it.copy(failure = Event(e)) }
            }
            finally {
                _state.update { it.copy(loading = false) }
            }
        }
    }
}