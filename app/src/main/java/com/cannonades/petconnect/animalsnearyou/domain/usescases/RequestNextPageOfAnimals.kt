package com.cannonades.petconnect.animalsnearyou.domain.usescases

import android.util.Log
import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfAnimals @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int
    ): Pagination {
        return withContext(dispatchersProvider.io()) {
            val (animals, pagination) = animalRepository.requestMoreAnimalsFromAPI(pageToLoad, Pagination.DEFAULT_PAGE_SIZE)

            Log.i("DHR", "DOMAIN layer (usecase) $animals")
            Log.i("DHR", "DOMAIN layer (usecase) $pagination")

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException()
            }

            animalRepository.storeAnimalsInDb(animals)

            return@withContext pagination
        }
    }
}