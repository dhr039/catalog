package com.cannonades.petconnect.common.di

import com.cannonades.petconnect.breeds.data.PetFaceBreedCategoriesCategoriesRepository
import com.cannonades.petconnect.common.data.PetFaceAnimalRepository
import com.cannonades.petconnect.categories.data.PetFaceCategoriesRepository
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.domain.repositories.BreedCategoriesRepository
import com.cannonades.petconnect.common.domain.repositories.CategoriesRepository
import com.cannonades.petconnect.common.utils.CoroutineDispatchersProvider
import com.cannonades.petconnect.common.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityRetainedModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAnimalRepository(repository: PetFaceAnimalRepository): AnimalRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindCategoryRepository(repository: PetFaceCategoriesRepository): CategoriesRepository

    @Binds
    @ActivityRetainedScoped
    abstract fun bindBreedCategoryRepository(repository: PetFaceBreedCategoriesCategoriesRepository): BreedCategoriesRepository

    @Binds
    abstract fun bindDispatchersProvider(dispatchersProvider: CoroutineDispatchersProvider):
            DispatchersProvider

//  companion object {
//    @Provides
//    fun provideCompositeDisposable() = CompositeDisposable()
//  }
}
