package com.cannonades.petconnect.common.data.api.model.mappers

import com.cannonades.petconnect.common.data.api.model.ApiAnimal
import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.Breed
import com.cannonades.petconnect.common.domain.model.Weight
import javax.inject.Inject

class ApiAnimalMapper @Inject constructor(private val apiPhotoMapper: ApiPhotoMapper) :
    ApiMapper<ApiAnimal, Animal> {

    override fun mapToDomain(apiEntity: ApiAnimal): Animal {
        return Animal(
            id = apiEntity.id.orEmpty(), //TODO: check this, what if id is empty/null ?
            photo = apiPhotoMapper.mapToDomain(apiEntity),
            breeds = if (apiEntity.breeds.isNullOrEmpty()) {
                null
            } else {
                listOf<Breed>(
                    Breed(
                        id = apiEntity.breeds[0].id ?: throw MappingException("Breed ID cannot be null"),
                        name = apiEntity.breeds[0].name ?: throw MappingException("breed name cannot be ampty"),
                        weight = if (apiEntity.breeds[0].weight == null) {
                            null
                        } else {
                            Weight(
                                imperial = apiEntity.breeds[0].weight?.imperial,
                                metric = apiEntity.breeds[0].weight?.metric,
                            )
                        },
                        cfaUrl = apiEntity.breeds[0].cfaUrl,
                        vetstreetUrl = apiEntity.breeds[0].vetstreetUrl,
                        temperament = apiEntity.breeds[0].temperament,
                        origin = apiEntity.breeds[0].origin,
                        countryCodes = apiEntity.breeds[0].countryCodes,
                        countryCode = apiEntity.breeds[0].countryCode,
                        description = apiEntity.breeds[0].description,
                        lifeSpan = apiEntity.breeds[0].lifeSpan,
                        indoor = apiEntity.breeds[0].indoor,
                        altNames = apiEntity.breeds[0].altNames,
                        adaptability = apiEntity.breeds[0].adaptability,
                        affectionLevel = apiEntity.breeds[0].affectionLevel,
                        childFriendly = apiEntity.breeds[0].childFriendly,
                        dogFriendly = apiEntity.breeds[0].dogFriendly,
                        energyLevel = apiEntity.breeds[0].energyLevel,
                        grooming = apiEntity.breeds[0].grooming,
                        healthIssues = apiEntity.breeds[0].healthIssues,
                        intelligence = apiEntity.breeds[0].intelligence,
                        sheddingLevel = apiEntity.breeds[0].sheddingLevel,
                        socialNeeds = apiEntity.breeds[0].socialNeeds,
                        strangerFriendly = apiEntity.breeds[0].strangerFriendly,
                        vocalisation = apiEntity.breeds[0].vocalisation,
                        experimental = apiEntity.breeds[0].experimental,
                        hairless = apiEntity.breeds[0].hairless,
                        natural = apiEntity.breeds[0].natural,
                        rare = apiEntity.breeds[0].rare,
                        rex = apiEntity.breeds[0].rex,
                        suppressedTail = apiEntity.breeds[0].suppressedTail,
                        shortLegs = apiEntity.breeds[0].shortLegs,
                        wikipediaUrl = apiEntity.breeds[0].wikipediaUrl,
                        hypoallergenic = apiEntity.breeds[0].hypoallergenic,
                        referenceImageId = apiEntity.breeds[0].referenceImageId
                    )
                )
            }
        )
    }

}