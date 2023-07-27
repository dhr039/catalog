package com.cannonades.petconnect.categories.domain.usecases

import com.cannonades.petconnect.common.domain.model.Category
import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import javax.inject.Inject

class GetCategoryByIdFromCacheUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(categId: String): Category {
        return categoriesRepository.getCategoryByIdFromDb(categId)
    }
}