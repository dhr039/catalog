package com.cannonades.petconnect.common.domain.repositories

import com.cannonades.petconnect.common.data.api.model.ApiCategory
import com.cannonades.petconnect.common.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun getCategoriesFromDb(): Flow<List<Category>>
    suspend fun requestCategoriesFromAPI(): List<Category>
    suspend fun storeCategoriesInDb(categories: List<Category>)
}