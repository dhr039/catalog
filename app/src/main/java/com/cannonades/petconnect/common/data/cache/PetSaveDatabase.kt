package com.cannonades.petconnect.common.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cannonades.petconnect.common.data.cache.daos.AnimalsDao
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimal
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalBreedCrossRef
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreed
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreedCategory
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory

@Database(
    entities = [
        CachedBreed::class,
        CachedAnimal::class,
        CachedAnimalBreedCrossRef::class,
        CachedCategory::class,
        CachedBreedCategory::class
    ],
    version = 1
)
abstract class PetSaveDatabase : RoomDatabase() {
    abstract fun animalsDao(): AnimalsDao
}
