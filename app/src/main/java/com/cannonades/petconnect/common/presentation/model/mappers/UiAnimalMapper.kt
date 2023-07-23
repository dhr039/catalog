package com.cannonades.petconnect.common.presentation.model.mappers

import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.presentation.model.UIAnimal
import com.cannonades.petconnect.common.presentation.model.UIBreed
import com.cannonades.petconnect.common.presentation.model.UIWeight
import javax.inject.Inject

class UiAnimalMapper @Inject constructor(): UiMapper<Animal, UIAnimal> {

    override fun mapToView(input: Animal): UIAnimal {
        val uiBreeds = input.breeds?.map { breed ->
            UIBreed(
                weight = breed.weight?.let { UIWeight(it.imperial, it.metric) },
                id = breed.id,
                name = breed.name,
                cfaUrl = breed.cfaUrl,
                vetstreetUrl = breed.vetstreetUrl,
                temperament = breed.temperament,
                origin = breed.origin,
                countryCodes = breed.countryCodes,
                countryCode = breed.countryCode,
                description = breed.description,
                lifeSpan = breed.lifeSpan,
                indoor = breed.indoor,
                altNames = breed.altNames,
                adaptability = breed.adaptability,
                affectionLevel = breed.affectionLevel,
                childFriendly = breed.childFriendly,
                dogFriendly = breed.dogFriendly,
                energyLevel = breed.energyLevel,
                grooming = breed.grooming,
                healthIssues = breed.healthIssues,
                intelligence = breed.intelligence,
                sheddingLevel = breed.sheddingLevel,
                socialNeeds = breed.socialNeeds,
                strangerFriendly = breed.strangerFriendly,
                vocalisation = breed.vocalisation,
                experimental = breed.experimental,
                hairless = breed.hairless,
                natural = breed.natural,
                rare = breed.rare,
                rex = breed.rex,
                suppressedTail = breed.suppressedTail,
                shortLegs = breed.shortLegs,
                wikipediaUrl = breed.wikipediaUrl,
                hypoallergenic = breed.hypoallergenic,
                referenceImageId = breed.referenceImageId
            )
        }

        return UIAnimal(
            id = input.id,
            name = input.photo.url, // TODO: change to something that makes sense
            photo = input.photo.url,
            width = input.photo.width,
            height = input.photo.height,
            breeds = uiBreeds
        )
    }
}
