package com.cannonades.petconnect.common.data.api

import com.cannonades.petconnect.common.data.api.model.ApiAnimal
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface PetFaceApi {

    @Headers("x-api-key: ${ApiConstants.KEY}") //TODO: see how to re-use x-api-key for all calls - DRY
    @GET(ApiConstants.ANIMALS_ENDPOINT)
    suspend fun getAnimals(
        @Query(ApiParameters.PAGE) pageToLoad: Int,
        @Query(ApiParameters.LIMIT) pageSize: Int,
        @Query(ApiParameters.ORDER) order: String = "DESC",
        @Query(ApiParameters.HAS_BREEDS) hasBreeds: Boolean = true,
    ): Response<List<ApiAnimal>>

}