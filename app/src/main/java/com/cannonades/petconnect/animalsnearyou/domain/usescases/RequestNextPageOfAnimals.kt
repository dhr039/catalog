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
        pageToLoad: Int,
        pageSize: Int = Pagination.DEFAULT_PAGE_SIZE
    ): Pagination {
        return withContext(dispatchersProvider.io()) {
            val (animals, pagination) = animalRepository.requestMoreAnimals(pageToLoad, pageSize)

            Log.i("DOMAIN layer (usecase)", animals.toString())
            Log.i("DOMAIN layer (usecase)", pagination.toString())

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException("No more animals :(")
            }

            animalRepository.storeAnimals(animals)

            return@withContext pagination
        }
    }
}