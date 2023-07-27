package com.cannonades.petconnect.breeds.data

import android.util.Log
import com.cannonades.petconnect.common.data.api.PetFaceApi
import com.cannonades.petconnect.common.data.api.model.ApiBreedCategory
import com.cannonades.petconnect.common.data.api.model.mappers.ApiBreedCategoryMapper
import com.cannonades.petconnect.common.data.cache.Cache
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedBreedCategory
import com.cannonades.petconnect.common.domain.model.BreedCategory
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.repositories.BreedCategoriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class PetFaceBreedCategoriesCategoriesRepository @Inject constructor(
    private val api: PetFaceApi,
    private val cache: Cache,
    private val apiCategoryMapper: ApiBreedCategoryMapper
) : BreedCategoriesRepository {

    override fun getBreedCategoriesFromDb(): Flow<List<BreedCategory>> {
        return cache.getBreedCategories().map { categList -> categList.map { it.toBreedCategoryDomain() } }
    }

    override suspend fun getBreedCategoryByIdFromDb(categId: String): BreedCategory {
        return cache.getBreedCategoryById(categId).toBreedCategoryDomain()
    }

    override suspend fun requestBreedCategoriesFromAPI(): List<BreedCategory> {
        try {
            val response: Response<List<ApiBreedCategory>> = api.getBreedCategories()
            return response.body()?.map { apiCategoryMapper.mapToDomain(it) } ?: emptyList()
        } catch (exception: HttpException) {
            throw NetworkException
        } catch (e: Exception) {
            Log.e("PetFaceBreedsCategoriesRepository", "throwing $e")
            throw e
        }
    }

    override suspend fun storeBreedCategoriesInDb(categories: List<BreedCategory>) {
        cache.storeBreedCategories(categories.map { CachedBreedCategory.fromDomain(it) })
    }

}