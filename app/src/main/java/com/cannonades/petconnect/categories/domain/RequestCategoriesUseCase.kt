package com.cannonades.petconnect.categories.domain

import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    suspend operator fun invoke() {
        withContext(dispatchersProvider.io()) {
            val categories = categoriesRepository.requestCategoriesFromAPI()

            categoriesRepository.storeCategoriesInDb(categories)
        }
    }
}