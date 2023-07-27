package com.cannonades.petconnect.breeds.domain

import com.cannonades.petconnect.common.domain.model.Breed
import com.cannonades.petconnect.common.domain.model.BreedCategory
import com.cannonades.petconnect.common.domain.repositories.BreedCategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import javax.inject.Inject

class GetBreedByIdFromCacheUseCase @Inject constructor(
    private val breedCategoriesRepository: BreedCategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(breedCategId: String): BreedCategory {
        return breedCategoriesRepository.getBreedCategoryByIdFromDb(breedCategId)
    }

}