package com.cannonades.petconnect.feature.breeds.domain.repositories

import com.cannonades.petconnect.common.domain.model.BreedCategory
import kotlinx.coroutines.flow.Flow

interface BreedCategoriesRepository {
    fun getBreedCategoriesFromDb(): Flow<List<BreedCategory>>
    suspend fun getBreedCategoryByIdFromDb(categId: String): BreedCategory
    suspend fun requestBreedCategoriesFromAPI(): List<BreedCategory>
    suspend fun storeBreedCategoriesInDb(categories: List<BreedCategory>)
}