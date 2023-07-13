package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cannonades.petconnect.common.domain.model.Category

@Entity(tableName = "categories")
data class CachedCategory(
    @PrimaryKey
    val categoryId: Int,
    val name: String,
    val isChecked: Boolean,
) {
    companion object {
        fun fromDomain(domainModel: Category): CachedCategory {
            return CachedCategory(
                categoryId = domainModel.id,
                name = domainModel.name,
                isChecked = domainModel.isChecked
            )
        }
    }

    fun toCategoryDomain(): Category {
        return Category(
            id = categoryId,
            name = name,
            isChecked = isChecked
        )
    }
}