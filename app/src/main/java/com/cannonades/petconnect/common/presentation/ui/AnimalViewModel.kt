package com.cannonades.petconnect.common.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.usecases.GetAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnimalViewModel @Inject constructor(
    private val getAnimalUseCase: GetAnimalUseCase
) : ViewModel() {

    private val _animal = MutableStateFlow<Animal?>(null)
    val animal: StateFlow<Animal?> = _animal

    fun loadAnimal(id: String) {
        viewModelScope.launch {
            val animal = getAnimalUseCase(id)
            _animal.emit(animal)
        }
    }
}
