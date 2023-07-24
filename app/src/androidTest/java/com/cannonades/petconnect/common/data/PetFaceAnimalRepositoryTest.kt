package com.cannonades.petconnect.common.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.cannonades.petconnect.common.data.di.CacheModule
import com.cannonades.petconnect.common.data.api.PetFaceApi
import com.cannonades.petconnect.common.data.api.model.mappers.ApiAnimalMapper
import com.cannonades.petconnect.common.data.api.utils.FakeServer
import com.cannonades.petconnect.common.data.cache.Cache
import com.cannonades.petconnect.common.data.cache.PetSaveDatabase
import com.cannonades.petconnect.common.data.cache.RoomCache
import com.cannonades.petconnect.common.data.cache.daos.AnimalsDao
import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.Breed
import com.cannonades.petconnect.common.domain.model.Photo
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.google.common.truth.Truth.assertThat
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * https://developer.android.com/kotlin/flow/test
 * */
@HiltAndroidTest
@UninstallModules(CacheModule::class)
class PetFaceAnimalRepositoryTest {
    private val fakeServer = FakeServer()
    private lateinit var repository: AnimalRepository
    private lateinit var api: PetFaceApi
    private lateinit var cache: Cache

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var database: PetSaveDatabase

    @Inject
    lateinit var retrofitBuilder: Retrofit.Builder

    @Inject
    lateinit var apiAnimalMapper: ApiAnimalMapper

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestCacheModule {

        @Binds
        abstract fun bindCache(cache: RoomCache): Cache

        companion object {

            @Provides
            @Singleton
            fun provideRoomDatabase(): PetSaveDatabase {
                return Room.inMemoryDatabaseBuilder(
                    InstrumentationRegistry.getInstrumentation().context,
                    PetSaveDatabase::class.java
                )
                    .allowMainThreadQueries()
                    .build()
            }

            @Provides
            fun provideAnimalsDao(petSaveDatabase: PetSaveDatabase): AnimalsDao =
                petSaveDatabase.animalsDao()

        }

    }

    @Before
    fun setup() {
        fakeServer.start()
        hiltRule.inject()

        api = retrofitBuilder
            .baseUrl(fakeServer.baseEndpoint)
            .build()
            .create(PetFaceApi::class.java)

        cache = RoomCache(database.animalsDao())

        repository = PetFaceAnimalRepository(
            api,
            cache,
            apiAnimalMapper
        )
    }

    @After
    fun teardown() {
        fakeServer.shutdown()
    }

    @Test
    fun requestMoreAnimals_success() = runBlocking {
        // Given
        val expectedAnimalId = "Hb2N6tYTJ"
        fakeServer.setHappyPathDispatcher()

        // When
        val paginatedAnimals = repository.requestMoreAnimalsFromAPI(
            pageToLoad = 1,
            numberOfItems = 100,
            listOf(),
            false
        )

        // Then
        val animal = paginatedAnimals.animals.first()
        assertThat(animal.id).isEqualTo(expectedAnimalId)
    }

    @Test
    fun storeNearbyAnimals_success(): Unit = runBlocking {
        // Given
        val expectedAnimals = listOf(
            Animal(
                id = "HHHHHH1",
                photo = Photo(url = "https://dkfjhglksjhd1.com", 256, 128, "image/png"),
                listOf(Breed(id = "breed1ID", name = "breed1Name"))
            ),
            Animal(
                id = "HHHHHH2",
                photo = Photo(url = "https://dkfjhglksjhd2.com", 52, 48, "image/gif"),
                listOf(Breed(id = "breed2ID", name = "breed1Name2"))
            ),
            Animal(
                id = "HHHHHH3",
                photo = Photo(url = "https://dkfjhglksjhd3.com", 200, 400, "image/jpeg"),
                listOf(Breed(id = "breed3ID", name = "breed1Name3"))
            )
        )

        // When
        repository.storeAnimalsInDb(expectedAnimals, false, false)

        // Then
        val animals = repository.getAnimalsWithCategoryFromDb().first()
        assertThat(animals).containsExactlyElementsIn(expectedAnimals)
    }

}