package com.cannonades.petconnect.common.data.cache

import com.cannonades.petconnect.common.data.cache.daos.AnimalsDao
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(private val animalsDao: AnimalsDao) : Cache {

    override fun getAnimals(): Flow<List<CachedAnimalAggregate>> {
        return animalsDao.getAllAnimals()
    }

    override suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>) {
        animalsDao.insertAnimals(animals)
    }

    override fun getCategories(): Flow<List<CachedCategory>> {
        return animalsDao.getAllCategories()
    }

    override suspend fun storeCategories(categories: List<CachedCategory>) {
        animalsDao.insertCategories(categories)
    }

    override suspend fun updateCategory(category: CachedCategory) {
        animalsDao.updateCategory(category)
    }

}