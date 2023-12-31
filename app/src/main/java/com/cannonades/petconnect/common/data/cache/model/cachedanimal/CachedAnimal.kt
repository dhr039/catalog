package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cannonades.petconnect.common.domain.model.Animal

@Entity(tableName = "animals")
data class CachedAnimal(
    @PrimaryKey
    val animalId: String,
    val url: String,
    val width: Long,
    val height: Long,
    val mime: String,
    val isWithCategories: Boolean,
    val isWithBreed: Boolean
) {
    companion object {

        fun fromDomain(domainModel: Animal, isWithCategories: Boolean, isWithBreed: Boolean): CachedAnimal {
            return CachedAnimal(
                animalId = domainModel.id,
                url = domainModel.photo.url,
                width = domainModel.photo.width,
                height = domainModel.photo.height,
                mime = domainModel.photo.mime,
                isWithCategories = isWithCategories,
                isWithBreed = isWithBreed
            )
        }

    }
}