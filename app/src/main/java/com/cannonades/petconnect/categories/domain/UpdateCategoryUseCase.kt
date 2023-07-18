package com.cannonades.petconnect.categories.domain

import com.cannonades.petconnect.common.domain.model.Category
import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import com.cannonades.petconnect.common.presentation.model.UICategory
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke(category: UICategory) {
        withContext(dispatchersProvider.io()) {
            categoriesRepository.updateCategoryInDb(Category.fromUICategory(category))
        }
    }
}