package com.cannonades.petconnect.categories.domain.usecases

import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import javax.inject.Inject

class GetAnimalsWithCategoryFromCacheUseCase @Inject constructor(private val animalRepository: AnimalRepository) {
    operator fun invoke() = animalRepository.getAnimalsWithCategoryFromDb()
}