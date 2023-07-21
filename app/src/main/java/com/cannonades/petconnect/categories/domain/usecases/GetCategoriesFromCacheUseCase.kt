package com.cannonades.petconnect.categories.domain.usecases

import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import javax.inject.Inject

class GetCategoriesFromCacheUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {
    operator fun invoke() = categoriesRepository.getCategoriesFromDb()
}