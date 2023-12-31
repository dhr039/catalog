package com.cannonades.petconnect.feature.categories.domain.repositories

import com.cannonades.petconnect.common.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategoriesFromDb(): Flow<List<Category>>
    suspend fun getCategoriesListFromDb(): List<Category>
    suspend fun getCategoryByIdFromDb(categId: String): Category
    suspend fun requestCategoriesFromAPI(): List<Category>
    suspend fun storeCategoriesInDb(categories: List<Category>)
    suspend fun updateCategoryInDb(category: Category)
}