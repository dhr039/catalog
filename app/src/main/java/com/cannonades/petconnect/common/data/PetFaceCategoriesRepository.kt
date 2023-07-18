package com.cannonades.petconnect.common.data

import android.util.Log
import com.cannonades.petconnect.common.data.api.PetFaceApi
import com.cannonades.petconnect.common.data.api.model.ApiCategory
import com.cannonades.petconnect.common.data.api.model.mappers.ApiCategoryMapper
import com.cannonades.petconnect.common.data.cache.Cache
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedCategory
import com.cannonades.petconnect.common.domain.model.Category
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class PetFaceCategoriesRepository @Inject constructor(
    private val api: PetFaceApi,
    private val cache: Cache,
    private val apiCategoryMapper: ApiCategoryMapper
) : CategoriesRepository {

    override fun getCategoriesFromDb(): Flow<List<Category>> {
        return cache.getCategories().map { categList -> categList.map { it.toCategoryDomain() } }
    }

    override suspend fun requestCategoriesFromAPI(): List<Category> {
        try {
            val response: Response<List<ApiCategory>> = api.getCategories()
            return response.body()?.map { apiCategoryMapper.mapToDomain(it) } ?: emptyList()
        } catch (exception: HttpException) {
            throw NetworkException
        }
    }

    override suspend fun storeCategoriesInDb(categories: List<Category>) {
        cache.storeCategories(categories.map { CachedCategory.fromDomain(it) })
    }

    override suspend fun updateCategoryInDb(category: Category) {
        Log.e("DHR", "updateCategoryInDb ::::: $category")
        cache.updateCategory(CachedCategory.fromDomain(category))
    }
}