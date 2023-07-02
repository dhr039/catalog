package com.cannonades.petconnect.common.data.api

import com.cannonades.petconnect.BuildConfig

object ApiConstants {
    const val BASE_ENDPOINT = "https://api.thecatapi.com/v1/"
    const val ANIMALS_ENDPOINT = "images/search"

    const val API_KEY = BuildConfig.API_KEY
}

object ApiParameters {
    const val LIMIT = "limit"
    const val PAGE = "page"
    const val ORDER = "order"
    const val HAS_BREEDS = "has_breeds"
    const val BREED_IDS = "breed_ids"
    const val CATEGORY_IDS = "category_ids"
    const val SUB_ID = "sub_id"
}