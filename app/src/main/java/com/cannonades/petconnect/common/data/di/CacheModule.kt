package com.cannonades.android.petface.common.data.di

import android.content.Context
import androidx.room.Room
import com.cannonades.petconnect.common.data.cache.Cache
import com.cannonades.petconnect.common.data.cache.PetSaveDatabase
import com.cannonades.petconnect.common.data.cache.RoomCache
import com.cannonades.petconnect.common.data.cache.daos.AnimalsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CacheModule {

    @Binds
    abstract fun bindCache(cache: RoomCache): Cache

    companion object {

        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): PetSaveDatabase {
            return Room.databaseBuilder(
                context,
                PetSaveDatabase::class.java,
                "petsave.db"
            ).build()
        }

        @Provides
        fun provideAnimalsDao(petSaveDatabase: PetSaveDatabase): AnimalsDao =
            petSaveDatabase.animalsDao()

    }

}