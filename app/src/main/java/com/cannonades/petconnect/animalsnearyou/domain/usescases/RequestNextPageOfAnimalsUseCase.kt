package com.cannonades.petconnect.animalsnearyou.domain.usescases

import com.cannonades.petconnect.common.domain.model.NoMoreAnimalsException
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestNextPageOfAnimalsUseCase @Inject constructor(
    private val animalRepository: AnimalRepository,
    private val categoriesRepository: CategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(
        pageToLoad: Int
    ): Pagination {
        return withContext(dispatchersProvider.io()) {

            if (pageToLoad < 1) {
                throw Exception("page cannot be lower than 1")
            }

            val listOfCheckedIds = categoriesRepository.getCategoriesListFromDb().filter { it.isChecked }.map { it.id }

            val (animals, pagination) = animalRepository.requestMoreAnimalsFromAPI(
                pageToLoad,
                Pagination.DEFAULT_PAGE_SIZE,
                categIds = listOfCheckedIds
            )

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException
            }

            animalRepository.storeAnimalsInDb(animals)

            return@withContext pagination
        }
    }
}