package com.cannonades.petconnect.feature.categories.domain.usecases

import com.cannonades.petconnect.common.data.di.ApplicationScope
import com.cannonades.petconnect.common.utils.DispatchersProvider
import com.cannonades.petconnect.feature.categories.domain.repositories.CategoriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val dispatchersProvider: DispatchersProvider,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    suspend operator fun invoke() {
        withContext(dispatchersProvider.io()) {
            val categories = categoriesRepository.requestCategoriesFromAPI()


            applicationScope.launch {
                //to test that the job is finished even after CancellationException uncomment:
//                Log.d("DHR", "before saving and delay")
//                delay(5000)
                categoriesRepository.storeCategoriesInDb(categories)
//                Log.d("DHR", "after saving")
            }

        }
    }
}