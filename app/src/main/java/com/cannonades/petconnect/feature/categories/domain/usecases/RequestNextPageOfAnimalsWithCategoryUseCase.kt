package com.cannonades.petconnect.feature.categories.domain.usecases

import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfAnimalsWithCategoryUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int,
        categId: String
    ): Pagination {
        return withContext(dispatchersProvider.io()) {

            if (pageToLoad < 1) {
                throw Exception("page cannot be lower than 1")
            }

            val (animals, pagination) = animalRepository.requestMoreAnimalsFromAPI(
                pageToLoad,
                Pagination.DEFAULT_PAGE_SIZE,
                listOf(categId), hasBreeds = false
            )

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException
            }

            animalRepository.storeAnimalsInDb(animals, isWithCategories = true, isWithBreed =  false)

            return@withContext pagination
        }
    }
}