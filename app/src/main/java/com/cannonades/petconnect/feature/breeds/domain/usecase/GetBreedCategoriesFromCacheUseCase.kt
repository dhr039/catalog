package com.cannonades.petconnect.feature.breeds.domain.usecase

import com.cannonades.petconnect.common.domain.model.BreedCategory
import com.cannonades.petconnect.feature.breeds.domain.repositories.BreedCategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetBreedCategoriesFromCacheUseCase @Inject constructor(
    private val breedCategoriesRepository: BreedCategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    operator fun invoke(): Flow<List<BreedCategory>> =
        breedCategoriesRepository.getBreedCategoriesFromDb()
            .flowOn(dispatchersProvider.io())
}