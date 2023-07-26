package com.cannonades.petconnect.breeds.domain

import android.util.Log
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

            /**
             *  for some reason server doesn't return cats of required breed
             *  if hasBreeds = true. Have to set it to false:
             * */
            val (animals, pagination) = animalRepository.requestMoreAnimalsFromAPI(
                pageToLoad = pageToLoad,
                pageSize = Pagination.DEFAULT_PAGE_SIZE,
                categIds = listOf(),
                breedIds = listOf(breedId),
                hasBreeds = false
            )

            if (animals.isEmpty()) {
                throw NoMoreAnimalsException
            }

            Log.e("DHR", "SIZE before: ${animalRepository.getAnimalsWithBreedListFromDb().size}")
            animalRepository.deleteAllAnimalsWithBreedCategories()
            Log.e("DHR", "SIZE after: ${animalRepository.getAnimalsWithBreedListFromDb().size}")

            //FIXME: check why the db is cleared but the previous cats are still displayed
            animalRepository.storeAnimalsInDb(animals, isWithCategories = false, isWithBreed =  true)

            return@withContext pagination
        }
    }
}