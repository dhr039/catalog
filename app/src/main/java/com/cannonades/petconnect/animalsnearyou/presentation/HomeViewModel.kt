package com.cannonades.petconnect.animalsnearyou.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.animalsnearyou.domain.usescases.GetAnimalsNoCategoryFromCacheUseCase
import com.cannonades.petconnect.animalsnearyou.domain.usescases.RequestNextPageOfAnimalsNoCategoryUseCase
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
class HomeViewModel @Inject constructor(
    private val requestNextPageOfAnimals: RequestNextPageOfAnimalsNoCategoryUseCase,
    private val getAnimalsNoCategory: GetAnimalsNoCategoryFromCacheUseCase,
    private val uiAnimalMapper: UiAnimalMapper,
) : ViewModel() {

    /*used to prevent loadAnimalsIfEmpty() being called before onNewAnimalList()*/
    private val isInitialListLoaded = MutableStateFlow(false)

    init {
        viewModelScope.launch {
            isInitialListLoaded.collect {
                if (it) {
                    /*the first call after a fresh app install when db is still empty*/
                    loadAnimalsIfEmpty()
                }
            }
        }
        viewModelScope.launch {
            getAnimalsNoCategory().collect { animals ->
                onNewAnimalList(animals.map { animal -> uiAnimalMapper.mapToView(animal) })
            }
        }
    }

    private val _state = MutableStateFlow(AnimalsListViewState())
    private var currentPage = 0
    private val pageMutex = Mutex()

    val state: StateFlow<AnimalsListViewState> = _state.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RequestMoreAnimals -> loadNextAnimalPage()
        }
    }

    private fun loadAnimalsIfEmpty() {
        if (state.value.animals.isEmpty()) {
            loadNextAnimalPage()
        }
    }

    private fun onNewAnimalList(animals: List<UIAnimal>) {
        val updatedAnimalSet = (state.value.animals + animals).toSet()

        _state.update { oldState ->
            oldState.copy(animals = updatedAnimalSet.toList())
        }
        isInitialListLoaded.value = true
    }

    private fun loadNextAnimalPage() {
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

                    val pagination = requestNextPageOfAnimals(++currentPage)
                    currentPage = pagination.currentPage
                }

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
                if (!hasMoreAnimalsBeenHandled) {
                    _state.update {
                        it.copy(loading = false, failure = Event(failure))
                    }
                    hasMoreAnimalsBeenHandled = true
                }
            }
        }
    }
}