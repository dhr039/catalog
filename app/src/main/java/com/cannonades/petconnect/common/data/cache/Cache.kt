package com.cannonades.petconnect.common.data.cache

import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreedCategory
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import kotlinx.coroutines.flow.Flow

interface Cache {

    suspend fun deleteAllAnimalsWithBreedCategories()

    fun getAnimalsWithBreed(): Flow<List<CachedAnimalAggregate>>

    fun getAnimalsWithBreedList(): List<CachedAnimalAggregate>

    fun getAnimalsNoCategory(): Flow<List<CachedAnimalAggregate>>

    fun getAnimalsWithCategory(): Flow<List<CachedAnimalAggregate>>

    suspend fun deleteAllAnimalsWithCategories()

    suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>)

    fun getBreedCategories(): Flow<List<CachedBreedCategory>>

    suspend fun storeBreedCategories(categories: List<CachedBreedCategory>)

    fun getCategories(): Flow<List<CachedCategory>>

    fun getCategoriesList(): List<CachedCategory>

    suspend fun storeCategories(categories: List<CachedCategory>)

    suspend fun updateCategory(category: CachedCategory)

    suspend fun getAnimalById(id: String): CachedAnimalAggregate?
}