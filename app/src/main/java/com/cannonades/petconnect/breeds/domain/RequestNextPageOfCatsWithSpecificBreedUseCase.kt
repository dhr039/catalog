package com.cannonades.petconnect.breeds.domain

import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfCatsWithSpecificBreedUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        breedId: String
    ): Pagination {
        return withContext(dispatchersProvider.io()) {

            if (pageToLoad < 1) {
                throw Exception("page cannot be lower than 1")
            }

            val (animals, pagination) = animalRepository.requestMoreAnimalsByBreedFromAPI(
                pageToLoad = pageToLoad,
                pageSize = Pagination.DEFAULT_PAGE_SIZE,
                breedIds = listOf(breedId),
            )

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException
            }

            animalRepository.storeAnimalsInDb(animals, isWithCategories = false, isWithBreed =  true)

            return@withContext pagination
        }
    }
}