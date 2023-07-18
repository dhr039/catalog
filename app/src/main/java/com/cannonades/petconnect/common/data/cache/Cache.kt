package com.cannonades.petconnect.common.data.cache

import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import kotlinx.coroutines.flow.Flow

interface Cache {

    fun getAnimals(): Flow<List<CachedAnimalAggregate>>

    suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>)

    fun getCategories(): Flow<List<CachedCategory>>

    suspend fun storeCategories(categories: List<CachedCategory>)

    suspend fun updateCategory(category: CachedCategory)
}