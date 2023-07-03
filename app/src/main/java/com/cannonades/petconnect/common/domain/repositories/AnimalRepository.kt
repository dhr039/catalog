package com.cannonades.petconnect.common.domain.repositories

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.pagination.PaginatedAnimals
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    fun getAnimalsFromDb(): Flow<List<Animal>>
    suspend fun requestMoreAnimalsFromAPI(pageToLoad: Int, numberOfItems: Int): PaginatedAnimals
    suspend fun storeAnimalsInDb(animals: List<Animal>)

//  TODO: Uncomment for remote search
//  suspend fun searchAnimalsRemotely(
//      pageToLoad: Int,
//      searchParameters: SearchParameters,
//      numberOfItems: Int
//  ): PaginatedAnimals
}
