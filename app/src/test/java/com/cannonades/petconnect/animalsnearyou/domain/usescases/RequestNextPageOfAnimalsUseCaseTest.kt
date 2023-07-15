package com.cannonades.petconnect.animalsnearyou.domain.usescases


import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.Photo
import com.cannonades.petconnect.common.domain.model.pagination.PaginatedAnimals
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import com.cannonades.petconnect.common.utils.DispatchersProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TestDispatchersProvider : DispatchersProvider {
    override fun io() = Dispatchers.Unconfined
}

@ExperimentalCoroutinesApi
class RequestNextPageOfAnimalsUseCaseTest {
    // Create a mock instance of AnimalRepository
    private val animalRepository = mock(AnimalRepository::class.java)

    // Create a mock instance of DispatchersProvider
    private val dispatchersProvider = TestDispatchersProvider()

    // Create an instance of RequestNextPageOfAnimalsUseCase with the mocked AnimalRepository and DispatchersProvider
    private lateinit var requestNextPageOfAnimalsUseCase: RequestNextPageOfAnimalsUseCase

    @Before
    fun setup() {
        requestNextPageOfAnimalsUseCase =
            RequestNextPageOfAnimalsUseCase(animalRepository, dispatchersProvider)
    }

    @Test
    fun `request animals from API with valid page`() = runTest {
        val pagination = Pagination(1, 5)
        val animals = listOf(
            Animal(
                id = "1",
                photo = Photo("some_url1", height = 128, width = 128, mime = "image/png"),
                breeds = listOf()
            ),
            Animal(
                id = "2",
                photo = Photo("some_url2", height = 128, width = 128, mime = "image/png"),
                breeds = listOf()
            ),
            Animal(
                id = "3",
                photo = Photo("some_url3", height = 128, width = 128, mime = "image/png"),
                breeds = listOf()
            ),
            Animal(
                id = "4",
                photo = Photo("some_url4", height = 128, width = 128, mime = "image/png"),
                breeds = listOf()
            ),
        )

        // Define what should happen when requestMoreAnimalsFromAPI is called
        `when`(
            animalRepository.requestMoreAnimalsFromAPI(
                anyInt(),
                anyInt(),
                anyList()
            )
        ).thenReturn(
            PaginatedAnimals(animals, pagination)
        )

        requestNextPageOfAnimalsUseCase.invoke(1)

        // Verify if the methods requestMoreAnimalsFromAPI and storeAnimalsInDb were called exactly once
        verify(animalRepository, times(1)).requestMoreAnimalsFromAPI(
            1,
            Pagination.DEFAULT_PAGE_SIZE,
            emptyList()
        )
        verify(animalRepository, times(1)).storeAnimalsInDb(animals)
    }

    @Test(expected = Exception::class)
    fun `request animals from API with invalid page`() = runTest {
        requestNextPageOfAnimalsUseCase.invoke(0)
    }
}


