package com.cannonades.petconnect.common.presentation.model

data class UIAnimal(
    val id: String,
    val name: String,
    val photo: String,
    val width: Long,
    val height: Long,
    val breeds: List<UIBreed>?
)


data class UIBreed(
    val weight: UIWeight? = null,
    val id: String,
    val name: String,
    val cfaUrl: String? = null,
    val vetstreetUrl: String? = null,
    val temperament: String? = null,
    val origin: String? = null,
    val countryCodes: String? = null,
    val countryCode: String? = null,
    val description: String? = null,
    val lifeSpan: String? = null,
    val indoor: Int? = null,
    val altNames: String? = null,
    val adaptability: Int? = null,
    val affectionLevel: Int? = null,
    val childFriendly: Int? = null,
    val dogFriendly: Int? = null,
    val energyLevel: Int? = null,
    val grooming: Int? = null,
    val healthIssues: Int? = null,
    val intelligence: Int? = null,
    val sheddingLevel: Int? = null,
    val socialNeeds: Int? = null,
    val strangerFriendly: Int? = null,
    val vocalisation: Int? = null,
    val experimental: Int? = null,
    val hairless: Int? = null,
    val natural: Int? = null,
    val rare: Int? = null,
    val rex: Int? = null,
    val suppressedTail: Int? = null,
    val shortLegs: Int? = null,
    val wikipediaUrl: String? = null,
    val hypoallergenic: Int? = null,
    val referenceImageId: String? = null
)

data class UIWeight(
    val imperial: String?,
    val metric: String?
)
