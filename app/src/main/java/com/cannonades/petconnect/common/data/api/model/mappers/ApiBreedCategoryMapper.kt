package com.cannonades.petconnect.common.data.api.model.mappers

import com.cannonades.petconnect.common.data.api.model.ApiBreedCategory
import com.cannonades.petconnect.common.domain.model.BreedCategory
import javax.inject.Inject

class ApiBreedCategoryMapper @Inject constructor() : ApiMapper<ApiBreedCategory, BreedCategory> {
    override fun mapToDomain(apiEntity: ApiBreedCategory): BreedCategory {
        return BreedCategory(
            id = apiEntity.id ?: "no_id",
            name = apiEntity.name ?: "no_name"
        )
    }
}