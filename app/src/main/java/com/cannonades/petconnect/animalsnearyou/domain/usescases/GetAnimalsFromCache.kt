package com.cannonades.petconnect.animalsnearyou.domain.usescases

import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAnimalsFromCache @Inject constructor(private val animalRepository: AnimalRepository) {
    operator fun invoke() = animalRepository.getAnimalsFromDb().map { all -> all.map { it } }
}