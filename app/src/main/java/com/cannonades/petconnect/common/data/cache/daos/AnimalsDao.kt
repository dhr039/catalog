package com.cannonades.petconnect.common.data.cache.daos

import androidx.room.*
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimal
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalBreedCrossRef
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreed
import kotlinx.coroutines.flow.Flow

@Dao
abstract class AnimalsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAnimal(animal: CachedAnimal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBreeds(breeds: List<CachedBreed>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAnimalBreedCrossRef(joins: List<CachedAnimalBreedCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    open suspend fun insertAnimalAggregate(animal: CachedAnimal, breeds: List<CachedBreed>) {
        insertAnimal(animal)
        insertBreeds(breeds)

        insertAnimalBreedCrossRef(breeds.map {
            CachedAnimalBreedCrossRef(
                animal.animalId,
                it.breedId
            )
        })
    }

    suspend fun insertAnimals(animalAggregates: List<CachedAnimalAggregate>) {
        for (animalAggregate in animalAggregates) {
            insertAnimalAggregate(animalAggregate.animal, animalAggregate.breeds)
        }
    }

    @Transaction
    @Query("SELECT * FROM animals")
    abstract fun getAllAnimals(): Flow<List<CachedAnimalAggregate>>

}