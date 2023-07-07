package com.cannonades.petconnect.common.data.api.model.mappers

import com.cannonades.petconnect.common.data.api.model.ApiCategory
import com.cannonades.petconnect.common.domain.model.Category
import javax.inject.Inject

class ApiCategoryMapper @Inject constructor() : ApiMapper<ApiCategory, Category> {
    override fun mapToDomain(apiEntity: ApiCategory): Category {
        return Category(
            id = apiEntity.id ?: 0,
            name = apiEntity.name.orEmpty()
        )
    }
}