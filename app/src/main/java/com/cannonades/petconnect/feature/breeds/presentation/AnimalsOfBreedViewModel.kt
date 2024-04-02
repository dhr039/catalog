package com.cannonades.petconnect.feature.breeds.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.feature.breeds.domain.usecase.ClearAnimalsWithBreedCategoryUseCase
import com.cannonades.petconnect.feature.breeds.domain.usecase.GetBreedByIdFromCacheUseCase
import com.cannonades.petconnect.feature.breeds.domain.usecase.GetCatsWithBreedFromCacheUseCase
import com.cannonades.petconnect.feature.breeds.domain.usecase.RequestNextPageOfCatsWithSpecificBreedUseCase
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.presentation.Event
import com.cannonades.petconnect.common.presentation.model.UIAnimal
import com.cannonades.petconnect.common.presentation.model.mappers.UiAnimalMapper
import com.cannonades.petconnect.common.presentation.ui.AnimalsListViewState
import com.cannonades.petconnect.common.utils.createExceptionHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


@HiltViewModel
class AnimalsOfBreedViewModel @Inject constructor(
    private val requestNextPageOfCatsWithSpecificBreedUseCase: RequestNextPageOfCatsWithSpecificBreedUseCase,
    private val getCatsWithBreedFromCacheUseCase: GetCatsWithBreedFromCacheUseCase,
    private val clearAnimalsWithBreedCategoryUseCase: ClearAnimalsWithBreedCategoryUseCase,
    private val getBreedByIdFromCacheUseCase: GetBreedByIdFromCacheUseCase,
    private val uiAnimalMapper: UiAnimalMapper,
) : ViewModel() {

    init {
        viewModelScope.launch {
            // on opening the screen delete previous data
            clearAnimalsWithBreedCategoryUseCase()

            getCatsWithBreedFromCacheUseCase().collect { animals ->
                onNewAnimalList(animals.map { animal -> uiAnimalMapper.mapToView(animal) })
            }
        }
    }

    private val _state = MutableStateFlow(AnimalsListViewState())
    private var currentPage = 0
    private val pageMutex = Mutex()

    val state: StateFlow<AnimalsListViewState> = _state.asStateFlow()

    fun onRequestMoreWithSpecificBreedEvent(breedId: String) {
        loadNextAnimalPage(breedId)
    }

    private fun onNewAnimalList(animals: List<UIAnimal>) {
        try {
            val updatedAnimalSet = (state.value.animals + animals).toSet()

            _state.update { oldState ->
                oldState.copy(animals = updatedAnimalSet.toList())
            }
        } catch (e: Exception) {
            /*Sometimes, onNewAnimalList can be called before state: StateFlow<AnimalsListViewState> has been initialized.
            * It throws a NullPointerException. Decided to use a try/catch rather than adding logic to prevent it. */
            Log.e("AnimalsOfBreedViewModel", "${e.stackTrace}")
        }
    }

    private fun loadNextAnimalPage(breedId: String) {
        _state.update { it.copy(loading = true) }
        val errorMessage = "Failed to fetch animals"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            try {

                /*if there were no animals do not make repeated requests*/
                if (hasMoreAnimalsBeenHandled) return@launch

                pageMutex.withLock {
                    /*if after you open the app there already is a saved list of items, call the API with the proper page number:*/
                    if (currentPage < 2 && state.value.animals.size > Pagination.DEFAULT_PAGE_SIZE) {
                        currentPage = state.value.animals.size / Pagination.DEFAULT_PAGE_SIZE
                    }

                    val pagination = requestNextPageOfCatsWithSpecificBreedUseCase(++currentPage, breedId = breedId)
                    currentPage = pagination.currentPage
                }

                val breedCategory = getBreedByIdFromCacheUseCase(breedId)
                _state.update { it.copy(categName = breedCategory.name) }

            } finally {
                /**
                 * Have to set loading back to false in the same scope were it was set to true.
                 * This will ensure that loading remains true for the entire duration of the
                 * network request, until it completes or fails.
                 * */
                _state.update { it.copy(loading = false) }
            }
        }
    }

    private var hasMoreAnimalsBeenHandled: Boolean = false

    private fun onFailure(failure: Throwable) {
        when (failure) {
            is NetworkException -> {
                _state.update { oldState ->
                    oldState.copy(loading = false, failure = Event(failure))
                }
            }

            is NoMoreAnimalsException -> {
                Log.e("DHR", "NoMoreAnimalsException hasMoreAnimalsBeenHandled:$hasMoreAnimalsBeenHandled")
                if (!hasMoreAnimalsBeenHandled) {
                    _state.update {
//                        it.copy(loading = false, failure = Event(failure))
                        it.copy(loading = false)
                    }
                    hasMoreAnimalsBeenHandled = true
                }
            }
        }
    }
}