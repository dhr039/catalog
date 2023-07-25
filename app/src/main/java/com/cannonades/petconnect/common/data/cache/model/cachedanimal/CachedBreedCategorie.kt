package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cannonades.petconnect.common.domain.model.BreedCategory

@Entity(tableName = "breed_categories")
data class CachedBreedCategory(
    @PrimaryKey
    val categoryId: String,
    val name: String,
) {
    companion object {
        fun fromDomain(domainModel: BreedCategory): CachedBreedCategory {
            return CachedBreedCategory(
                categoryId = domainModel.id,
                name = domainModel.name
            )
        }
    }

    fun toBreedCategoryDomain(): BreedCategory {
        return BreedCategory(
            id = categoryId.toString(),
            name = name,
        )
    }
}