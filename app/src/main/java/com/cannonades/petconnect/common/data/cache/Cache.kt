package com.cannonades.petconnect.common.data.cache

import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import kotlinx.coroutines.flow.Flow

interface Cache {

    fun getAnimals(): Flow<List<CachedAnimalAggregate>>

    suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>)
}