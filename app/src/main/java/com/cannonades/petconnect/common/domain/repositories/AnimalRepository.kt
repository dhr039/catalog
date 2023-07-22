package com.cannonades.petconnect.common.domain.repositories

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.pagination.PaginatedAnimals
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    fun getAnimalsNoCategoryFromDb(): Flow<List<Animal>>
    fun getAnimalsWithCategoryFromDb(): Flow<List<Animal>>
    suspend fun deleteAllAnimalsWithCategories()
    suspend fun requestMoreAnimalsFromAPI(pageToLoad: Int, numberOfItems: Int, categIds: List<Int> = listOf()): PaginatedAnimals
    suspend fun storeAnimalsInDb(animals: List<Animal>, isWithCategories: Boolean)
    suspend fun getAnimalFromDb(id: String): Animal?
}
