package com.cannonades.petconnect.common.data.api.model.mappers

import com.cannonades.petconnect.common.data.api.model.ApiAnimal
import com.cannonades.petconnect.common.domain.model.Photo
import javax.inject.Inject

class ApiPhotoMapper @Inject constructor() : ApiMapper<ApiAnimal, Photo> {

    override fun mapToDomain(apiEntity: ApiAnimal): Photo {
        return Photo(
            url = apiEntity.url.orEmpty(),
            width = apiEntity.width ?: 0,
            height = apiEntity.height ?: 0,
            mime = apiEntity.mimeType.orEmpty()
        )
    }

}