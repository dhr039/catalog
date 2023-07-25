package com.cannonades.petconnect.breeds.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.breeds.domain.GetBreedCategoriesFromCacheUseCase
import com.cannonades.petconnect.breeds.domain.RequestBreedCategoriesUseCase
import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.mappers.UIBreedCategoryMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreedCategoriesViewModel @Inject constructor(
    private val requestBreedCategoriesUseCase: RequestBreedCategoriesUseCase,
    private val getBreedsFromCache: GetBreedCategoriesFromCacheUseCase,
    private val uiBreedCategoryMapper: UIBreedCategoryMapper
) : ViewModel() {

    init {
        viewModelScope.launch {
            getBreedsFromCache().collect { categories ->
                onNewCategoriesList(categories.map { category -> uiBreedCategoryMapper.mapToView(category) })
            }
        }
    }

    private val _state = MutableStateFlow(BreedsCategoriesUiState())
    val breedsCategoriesUiStateStateFlow: StateFlow<BreedsCategoriesUiState> = _state.asStateFlow()

    private fun onNewCategoriesList(breeds: List<UIBreedCategory>) {
        if (breeds.isNotEmpty()) {
            _state.update { oldState ->
                oldState.copy(breeds = breeds.toList())
            }
        }
    }

    fun onEvent(event: BreedsEvent) {
        when (event) {
            is BreedsEvent.RequestMoreBreeds -> loadBreedCategories()
        }
    }

    private fun loadBreedCategories() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                requestBreedCategoriesUseCase()
            } catch (e: Exception){
                _state.update { it.copy(failure = Event(e)) }
            }
            finally {
                _state.update { it.copy(loading = false) }
            }
        }
    }
}