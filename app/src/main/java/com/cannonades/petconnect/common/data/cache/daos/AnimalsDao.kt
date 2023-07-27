package com.cannonades.petconnect.common.data.cache.daos

import androidx.room.*
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimal
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalBreedCrossRef
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreed
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreedCategory
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AnimalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAnimal(animals: List<CachedAnimal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBreeds(breeds: List<CachedBreed>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAnimalBreedCrossRef(joins: List<CachedAnimalBreedCrossRef>)

    @Transaction
    open suspend fun insertAnimals(animalAggregates: List<CachedAnimalAggregate>) {
        val animals = mutableListOf<CachedAnimal>()
        val breeds = mutableListOf<CachedBreed>()
        val joins = mutableListOf<CachedAnimalBreedCrossRef>()

        for (animalAggregate in animalAggregates) {
            animals.add(animalAggregate.animal)
            breeds.addAll(animalAggregate.breeds)

            joins.addAll(animalAggregate.breeds.map {
                CachedAnimalBreedCrossRef(
                    animalAggregate.animal.animalId,
                    it.breedId
                )
            })
        }

        insertAnimal(animals)
        insertBreeds(breeds)
        insertAnimalBreedCrossRef(joins)
    }

    @Transaction
    @Query("SELECT * FROM animals WHERE isWithBreed = 1")
    abstract fun getAllAnimalsWithBreed(): Flow<List<CachedAnimalAggregate>>

    @Transaction
    @Query("SELECT * FROM animals WHERE isWithBreed = 1")
    abstract fun getAllAnimalsWithBreedList(): List<CachedAnimalAggregate>

    @Query("DELETE FROM animals WHERE isWithBreed = 1")
    abstract suspend fun deleteAllAnimalsWithBreedCategories()

    @Transaction
    @Query("SELECT * FROM animals WHERE isWithCategories = 0 AND isWithBreed = 0")
    abstract fun getAllAnimalsNoCategory(): Flow<List<CachedAnimalAggregate>>

    @Transaction
    @Query("SELECT * FROM animals WHERE isWithCategories = 1")
    abstract fun getAllAnimalsWithCategory(): Flow<List<CachedAnimalAggregate>>

    @Query("DELETE FROM animals WHERE isWithCategories = 1")
    abstract suspend fun deleteAllAnimalsWithCategories()

    @Transaction
    @Query("SELECT * FROM breed_categories")
    abstract fun getAllBreedCategories(): Flow<List<CachedBreedCategory>>

    @Query("SELECT * FROM breed_categories WHERE categoryId = :categoryId LIMIT 1")
    abstract suspend fun getBreedCategById(categoryId: String): CachedBreedCategory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBreedCategories(categories: List<CachedBreedCategory>)

    @Transaction
    @Query("SELECT * FROM categories")
    abstract fun getAllCategories(): Flow<List<CachedCategory>>

    @Query("SELECT * FROM categories WHERE categoryId = :categoryId LIMIT 1")
    abstract suspend fun getCategById(categoryId: String): CachedCategory

    @Transaction
    @Query("SELECT * FROM categories")
    abstract fun getAllCategoriesList(): List<CachedCategory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertCategories(categories: List<CachedCategory>)

    @Update
    abstract suspend fun updateCategory(category: CachedCategory)

    @Transaction
    @Query("SELECT * FROM animals WHERE animalId = :id")
    abstract suspend fun getAnimalById(id: String): CachedAnimalAggregate?

}