package com.cannonades.petconnect.common.data.cache.model.cachedanimal

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cannonades.petconnect.common.domain.model.Breed

@Entity(tableName = "breeds")
data class CachedBreed(
    @PrimaryKey
    val breedId: String,
    val name: String,
    val cfaUrl: String?,
    val vetstreetUrl: String?,
    val temperament: String?,
    val origin: String?,
    val countryCodes: String?,
    val countryCode: String?,
    val description: String?,
    val lifeSpan: String?,
    val indoor: Int?,
    val altNames: String?,
    val adaptability: Int?,
    val affectionLevel: Int?,
    val childFriendly: Int?,
    val dogFriendly: Int?,
    val energyLevel: Int?,
    val grooming: Int?,
    val healthIssues: Int?,
    val intelligence: Int?,
    val sheddingLevel: Int?,
    val socialNeeds: Int?,
    val strangerFriendly: Int?,
    val vocalisation: Int?,
    val experimental: Int?,
    val hairless: Int?,
    val natural: Int?,
    val rare: Int?,
    val rex: Int?,
    val suppressedTail: Int?,
    val shortLegs: Int?,
    val wikipediaUrl: String?,
    val hypoallergenic: Int?,
    val referenceImageId: String?
) {
    fun toDomain(): Breed {
        return Breed(
            id = breedId,
            name = name,
            cfaUrl = cfaUrl,
            vetstreetUrl = vetstreetUrl,
            temperament = temperament,
            origin = origin,
            countryCodes = countryCodes,
            countryCode = countryCode,
            description = description,
            lifeSpan = lifeSpan,
            indoor = indoor,
            altNames = altNames,
            adaptability = adaptability,
            affectionLevel = affectionLevel,
            childFriendly = childFriendly,
            dogFriendly = dogFriendly,
            energyLevel = energyLevel,
            grooming = grooming,
            healthIssues = healthIssues,
            intelligence = intelligence,
            sheddingLevel = sheddingLevel,
            socialNeeds = socialNeeds,
            strangerFriendly = strangerFriendly,
            vocalisation = vocalisation,
            experimental = experimental,
            hairless = hairless,
            natural = natural,
            rare = rare,
            rex = rex,
            suppressedTail = suppressedTail,
            shortLegs = shortLegs,
            wikipediaUrl = wikipediaUrl,
            hypoallergenic = hypoallergenic,
            referenceImageId = referenceImageId
        )
    }
}