package com.cannonades.petconnect.categories.domain

import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesFromCache @Inject constructor(private val categoriesRepository: CategoriesRepository) {
    operator fun invoke() = categoriesRepository.getCategoriesFromDb().map { all -> all.map { it } }
}