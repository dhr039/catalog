package com.cannonades.petconnect.animalsnearyou.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.animalsnearyou.domain.usescases.GetAnimalsFromCache
import com.cannonades.petconnect.animalsnearyou.domain.usescases.RequestNextPageOfAnimals
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.model.NetworkUnavailableException
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
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val requestNextPageOfAnimals: RequestNextPageOfAnimals,
    private val getAnimals: GetAnimalsFromCache,
    private val uiAnimalMapper: UiAnimalMapper,
) : ViewModel() {

    init {
        viewModelScope.launch {
            getAnimals().collect { animals ->
                onNewAnimalList(animals.map { animal -> uiAnimalMapper.mapToView(animal) })
            }
        }
    }

    private val _state = MutableStateFlow(AnimalsListViewState())
    private var currentPage = 0

    val state: StateFlow<AnimalsListViewState> = _state.asStateFlow()

    /**
     * Mark the failure as handled in order to show the Snackbar exactly once.
     *
     * Explanation:
     * When you reach the end of the list a new page of data is requested.
     * If there is no more remote data an Exception is thrown and we show a Snackbar.
     * Since this function marks the failure as handled so we don't show the Snackbar again
     * and again, after the user clicks on Dismiss. It shows again since we are at the end
     * of the list and any user movement triggers a recomposition and a new call for data.
     * */
    fun onShowSnackbar() {
        _state.update { it.copy(failureHasBeenHandled = true) }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadAnimalsIfEmpty -> loadAnimalsIfEmpty()
            is HomeEvent.RequestMoreAnimals -> loadNextAnimalPage()
        }
    }

    private fun loadAnimalsIfEmpty() {
        if (state.value.animals.isEmpty()) {
            loadNextAnimalPage()
        }
    }

    private fun onNewAnimalList(animals: List<UIAnimal>) {
        if (animals.isNotEmpty()) {
            Log.d(
                "DHR",
                "Got more animals! ${animals.size} + ${animals[animals.size - 1].id} -- ${animals[0].id}"
            )
        }

        val updatedAnimalSet = (state.value.animals + animals).toSet()

        _state.update { oldState ->
            oldState.copy(animals = updatedAnimalSet.toList())
        }
    }

    private fun loadNextAnimalPage() {
        _state.update { it.copy(loading = true) }
        val errorMessage = "Failed to fetch animals"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            try {

                /*if after you open the app there already is a saved list of items, call the API with the proper page number:*/
                if (currentPage < 2 && state.value.animals.size > Pagination.DEFAULT_PAGE_SIZE) {
                    currentPage = state.value.animals.size / Pagination.DEFAULT_PAGE_SIZE
                }

                val pagination = requestNextPageOfAnimals(++currentPage)
                currentPage = pagination.currentPage
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

    private fun onFailure(failure: Throwable) {
        Log.e("AnimalsNearYouFragmentViewModel", "onFailure: $failure")
        when (failure) {
            is NetworkException,
            is NetworkUnavailableException -> {
                _state.update { oldState ->
                    oldState.copy(loading = false, failure = Event(failure))
                }
            }

            is NoMoreAnimalsException -> {
                _state.update {
                    it.copy(loading = false, noMoreAnimalsNearby = true, failure = Event(failure))
                }
            }
        }
    }
}