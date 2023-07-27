package com.cannonades.petconnect.feature.breeds.domain.usecase

import com.cannonades.petconnect.feature.breeds.domain.repositories.BreedCategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestBreedCategoriesUseCase @Inject constructor(
    private val breedCategoriesRepository: BreedCategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke() {
        withContext(dispatchersProvider.io()) {
            val breeds = breedCategoriesRepository.requestBreedCategoriesFromAPI()

            breedCategoriesRepository.storeBreedCategoriesInDb(breeds)
        }
    }
}