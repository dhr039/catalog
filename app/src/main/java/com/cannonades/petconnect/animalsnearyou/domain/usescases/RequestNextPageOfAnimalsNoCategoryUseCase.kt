package com.cannonades.petconnect.animalsnearyou.domain.usescases

import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfAnimalsNoCategoryUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int
    ): Pagination {
        return withContext(dispatchersProvider.io()) {

            if (pageToLoad < 1) {
                throw Exception("page cannot be lower than 1")
            }

            val (animals, pagination) = animalRepository.requestMoreAnimalsFromAPI(
                pageToLoad,
                Pagination.DEFAULT_PAGE_SIZE
            )

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException
            }

            animalRepository.storeAnimalsInDb(animals, false)

            return@withContext pagination
        }
    }
}