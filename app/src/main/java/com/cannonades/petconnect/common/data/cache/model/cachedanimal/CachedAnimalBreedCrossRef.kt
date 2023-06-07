package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["animalId","breedId"], indices = [Index("breedId")])
data class CachedAnimalBreedCrossRef(
    val animalId: String,
    val breedId: String
)