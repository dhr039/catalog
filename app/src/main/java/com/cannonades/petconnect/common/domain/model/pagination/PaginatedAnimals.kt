package com.cannonades.petconnect.common.domain.model.pagination

import com.cannonades.petconnect.common.domain.model.Animal

data class PaginatedAnimals(
    val animals: List<Animal>,
    val pagination: Pagination
)
