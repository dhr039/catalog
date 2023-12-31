package com.cannonades.petconnect.feature.home.domain.usescases

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAnimalsNoCategoryFromCacheUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    operator fun invoke(): Flow<List<Animal>> =
        animalRepository.getAnimalsNoCategoryFromDb()
            .flowOn(dispatchersProvider.io())
}