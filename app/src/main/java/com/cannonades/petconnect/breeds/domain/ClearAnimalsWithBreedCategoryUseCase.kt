package com.cannonades.petconnect.breeds.domain

import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ClearAnimalsWithBreedCategoryUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke() {
        withContext(dispatchersProvider.io()) {
            animalRepository.deleteAllAnimalsWithBreedCategories()
        }
    }
}