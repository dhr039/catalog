package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cannonades.petconnect.common.domain.model.Breed

@Entity(tableName = "breeds")
data class CachedBreed( //TODO: use the rest of the fields from the API
    @PrimaryKey
    val breedId: String,
    val name: String
) {
    fun toDomain(): Breed {
        return Breed(id = breedId, name=name)
    }
}