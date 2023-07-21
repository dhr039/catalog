package com.cannonades.petconnect.animalsnearyou.domain.usescases

import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import javax.inject.Inject

class GetAnimalsNoCategoryFromCacheUseCase @Inject constructor(private val animalRepository: AnimalRepository) {
    operator fun invoke() = animalRepository.getAnimalsNoCategoryFromDb()
}