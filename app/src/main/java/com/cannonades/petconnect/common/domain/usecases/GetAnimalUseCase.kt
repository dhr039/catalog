package com.cannonades.petconnect.common.domain.usecases

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAnimalUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(id: String) : Animal? {
        return withContext(dispatchersProvider.io()) {
            animalRepository.getAnimalFromDb(id)
        }
    }
}