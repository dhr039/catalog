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
                    animal.breeds.map { CachedBreed(it.id, it.name) }
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