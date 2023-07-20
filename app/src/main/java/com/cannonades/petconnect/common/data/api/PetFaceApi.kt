package com.cannonades.petconnect.common.data.api

import com.cannonades.petconnect.common.data.api.model.ApiAnimal
import com.cannonades.petconnect.common.data.api.model.ApiCategory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PetFaceApi {

    @Headers("x-api-key: ${ApiConstants.API_KEY}")
    @GET(ApiConstants.ANIMALS_ENDPOINT)
    suspend fun getAnimals(
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.LIMIT) pageSize: Int,
        @Query(ApiParameters.ORDER) order: String = "DESC",
        @Query(ApiParameters.HAS_BREEDS) hasBreeds: Boolean = false,
        @Query(ApiParameters.CATEGORY_IDS) categIds: String = "",
    ): Response<List<ApiAnimal>>

    @Headers("x-api-key: ${ApiConstants.API_KEY}")
    @GET(ApiConstants.CATEGORIES_ENDPOINT)
    suspend fun getCategories(
    ): Response<List<ApiCategory>>

}