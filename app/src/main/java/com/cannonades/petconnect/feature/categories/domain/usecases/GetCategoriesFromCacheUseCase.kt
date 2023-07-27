package com.cannonades.petconnect.feature.categories.domain.usecases

import com.cannonades.petconnect.common.domain.model.Category
import com.cannonades.petconnect.feature.categories.domain.repositories.CategoriesRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCategoriesFromCacheUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val dispatchersProvider: DispatchersProvider
) {
    operator fun invoke(): Flow<List<Category>> =
        categoriesRepository.getCategoriesFromDb()
            .flowOn(dispatchersProvider.io())
}