package com.cannonades.petconnect.common.domain.repositories

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.pagination.PaginatedAnimals
import kotlinx.coroutines.flow.Flow

interface AnimalRepository {
    fun getAnimalsNoCategoryFromDb(): Flow<List<Animal>>
    fun getAnimalsWithCategoryFromDb(): Flow<List<Animal>>
    fun getAnimalsWithBreedFromDb(): Flow<List<Animal>>
    fun getAnimalsWithBreedListFromDb(): List<Animal>
    suspend fun deleteAllAnimalsWithBreedCategories()
    suspend fun deleteAllAnimalsWithCategories()

    suspend fun requestMoreAnimalsFromAPI(
        pageToLoad: Int,
        pageSize: Int,
        categIds: List<Int> = listOf(),
        breedIds: List<String> = listOf(),
        hasBreeds: Boolean
    ): PaginatedAnimals

    suspend fun requestMoreAnimalsByBreedFromAPI(
        pageToLoad: Int,
        pageSize: Int,
        breedIds: List<String> = listOf(),
    ): PaginatedAnimals

    suspend fun storeAnimalsInDb(animals: List<Animal>, isWithCategories: Boolean, isWithBreed: Boolean)
    suspend fun getAnimalFromDb(id: String): Animal?
}
