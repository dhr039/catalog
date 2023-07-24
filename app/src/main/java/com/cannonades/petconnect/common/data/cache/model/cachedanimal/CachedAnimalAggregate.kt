package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.Breed
import com.cannonades.petconnect.common.domain.model.Photo

data class CachedAnimalAggregate(
    @Embedded
    val animal: CachedAnimal,
    @Relation(
        parentColumn = "animalId",
        entityColumn = "breedId",
        associateBy = Junction(CachedAnimalBreedCrossRef::class)
    )
    val breeds: List<CachedBreed>
) {
    companion object {
        fun fromDomain(animal: Animal, isWithCategories: Boolean): CachedAnimalAggregate {
            return CachedAnimalAggregate(
                animal = CachedAnimal.fromDomain(animal, isWithCategories),
                breeds = if (animal.breeds == null) {
                    emptyList()
                } else {
                    animal.breeds.map {
                        CachedBreed(
                            breedId = it.id,
                            name = it.name,
                            cfaUrl = it.cfaUrl,
                            vetstreetUrl = it.vetstreetUrl,
                            temperament = it.temperament,
                            origin = it.origin,
                            countryCodes = it.countryCodes,
                            countryCode = it.countryCode,
                            description = it.description,
                            lifeSpan = it.lifeSpan,
                            indoor = it.indoor,
                            altNames = it.altNames,
                            adaptability = it.adaptability,
                            affectionLevel = it.affectionLevel,
                            childFriendly = it.childFriendly,
                            dogFriendly = it.dogFriendly,
                            energyLevel = it.energyLevel,
                            grooming = it.grooming,
                            healthIssues = it.healthIssues,
                            intelligence = it.intelligence,
                            sheddingLevel = it.sheddingLevel,
                            socialNeeds = it.socialNeeds,
                            strangerFriendly = it.strangerFriendly,
                            vocalisation = it.vocalisation,
                            experimental = it.experimental,
                            hairless = it.hairless,
                            natural = it.natural,
                            rare = it.rare,
                            rex = it.rex,
                            suppressedTail = it.suppressedTail,
                            shortLegs = it.shortLegs,
                            wikipediaUrl = it.wikipediaUrl,
                            hypoallergenic = it.hypoallergenic,
                            referenceImageId = it.referenceImageId
                        )
                    }
                }
            )
        }
    }

    fun toAnimalDomain(): Animal {
        val listOfDomainBreeds = ArrayList<Breed>()
        for (breed in breeds) {
            val temp = breed.toDomain()
            listOfDomainBreeds += temp
        }
        return Animal(
            id = animal.animalId,
            photo = Photo(
                url = animal.url,
                width = animal.width,
                height = animal.height,
                mime = animal.mime
            ),
            breeds = listOfDomainBreeds
        )
    }
}