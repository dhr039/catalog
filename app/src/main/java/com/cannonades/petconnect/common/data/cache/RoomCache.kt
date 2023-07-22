package com.cannonades.petconnect.common.data.cache

import com.cannonades.petconnect.common.data.cache.daos.AnimalsDao
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(private val animalsDao: AnimalsDao) : Cache {

    override fun getAnimalsNoCategory(): Flow<List<CachedAnimalAggregate>> {
        return animalsDao.getAllAnimalsNoCategory()
    }

    override fun getAnimalsWithCategory(): Flow<List<CachedAnimalAggregate>> {
        return animalsDao.getAllAnimalsWithCategory()
    }

    override suspend fun deleteAllAnimalsWithCategories() {
        animalsDao.deleteAllAnimalsWithCategories()
    }

    override suspend fun storeNearbyAnimals(animals: List<CachedAnimalAggregate>) {
        animalsDao.insertAnimals(animals)
    }

    override fun getCategories(): Flow<List<CachedCategory>> {
        return animalsDao.getAllCategories()
    }

    override fun getCategoriesList(): List<CachedCategory> {
        return animalsDao.getAllCategoriesList()
    }

    override suspend fun storeCategories(categories: List<CachedCategory>) {
        animalsDao.insertCategories(categories)
    }

    override suspend fun updateCategory(category: CachedCategory) {
        animalsDao.updateCategory(category)
    }

    override suspend fun getAnimalById(id: String): CachedAnimalAggregate? {
        return animalsDao.getAnimalById(id)
    }

}