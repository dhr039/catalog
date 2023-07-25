package com.cannonades.petconnect.common.data.cache

import android.util.Log
import com.cannonades.petconnect.common.data.cache.daos.AnimalsDao
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreedCategory
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCache @Inject constructor(private val animalsDao: AnimalsDao) : Cache {
    override suspend fun deleteAllAnimalsWithBreedCategories() {
        animalsDao.deleteAllAnimalsWithBreedCategories()
    }

    override fun getAnimalsWithBreed(): Flow<List<CachedAnimalAggregate>> {
        return animalsDao.getAllAnimalsWithBreed()
    }

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

    override fun getBreedCategories(): Flow<List<CachedBreedCategory>> {
        return animalsDao.getAllBreedCategories()
    }

    override suspend fun storeBreedCategories(categories: List<CachedBreedCategory>) {
        animalsDao.insertBreedCategories(categories)
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