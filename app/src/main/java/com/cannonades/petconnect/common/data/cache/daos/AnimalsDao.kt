package com.cannonades.petconnect.common.data.cache.daos

import androidx.room.*
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimal
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalBreedCrossRef
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreed
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
    @Query("SELECT * FROM animals")
    abstract fun getAllAnimals(): Flow<List<CachedAnimalAggregate>>

    @Transaction
    @Query("SELECT * FROM categories")
    abstract fun getAllCategories(): Flow<List<CachedCategory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertCategories(categories: List<CachedCategory>)

}