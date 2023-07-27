package com.cannonades.petconnect.feature.categories.domain.usecases

import com.cannonades.petconnect.feature.categories.domain.repositories.CategoriesRepository
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