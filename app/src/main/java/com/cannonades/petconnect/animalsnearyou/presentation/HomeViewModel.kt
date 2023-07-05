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

    companion object {
        const val UI_PAGE_SIZE = Pagination.DEFAULT_PAGE_SIZE
    }

    init {
        subscribeToAnimalUpdates()
    }

    private fun subscribeToAnimalUpdates() {
        viewModelScope.launch {
            getAnimals().collect { animals ->
                onNewAnimalList(animals.map { animal -> uiAnimalMapper.mapToView(animal) })
            }
        }
    }

    private val _state = MutableStateFlow(HomeViewState())
    private var currentPage = 0

    val state: StateFlow<HomeViewState> = _state.asStateFlow()

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
                "HomeViewModel",
                "Got more animals! ${animals.size} + ${animals[animals.size - 1].id} -- ${animals[0].id}"
            )
        }

        val updatedAnimalSet = (state.value.animals + animals).toSet()

        _state.update { oldState ->
            oldState.copy(loading = false, animals = updatedAnimalSet.toList(), failure = null)
        }
    }

    private fun loadNextAnimalPage() {
        _state.update { it.copy(loading = true) }
        val errorMessage = "Failed to fetch animals"
        val exceptionHandler = viewModelScope.createExceptionHandler(errorMessage) { onFailure(it) }

        viewModelScope.launch(exceptionHandler) {
            val pagination = requestNextPageOfAnimals(++currentPage)
            currentPage = pagination.currentPage
        }
    }

    private fun onFailure(failure: Throwable) {
        Log.d("AnimalsNearYouFragmentViewModel", "onFailure: $failure")
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