package com.cannonades.petconnect.common.data

import com.cannonades.petconnect.common.data.api.PetFaceApi
import com.cannonades.petconnect.common.data.api.model.ApiAnimal
import com.cannonades.petconnect.common.data.api.model.mappers.ApiAnimalMapper
import com.cannonades.petconnect.common.data.cache.Cache
import com.cannonades.petconnect.common.data.cache.model.cachedanimal.CachedAnimalAggregate
import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.model.NetworkException
import com.cannonades.petconnect.common.domain.model.pagination.PaginatedAnimals
import com.cannonades.petconnect.common.domain.model.pagination.Pagination
import com.cannonades.petconnect.common.domain.repositories.AnimalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class PetFaceAnimalRepository @Inject constructor(
    private val api: PetFaceApi,
    private val cache: Cache,
    private val apiAnimalMapper: ApiAnimalMapper
) : AnimalRepository {

    override fun getAnimalsNoCategoryFromDb(): Flow<List<Animal>> {
        return cache.getAnimalsNoCategory().map { animalList -> animalList.map { it.toAnimalDomain() } }
    }

    override fun getAnimalsWithCategoryFromDb(): Flow<List<Animal>> {
        return cache.getAnimalsWithCategory().map { animalList -> animalList.map { it.toAnimalDomain() } }
    }

    override fun getAnimalsWithBreedFromDb(): Flow<List<Animal>> {
        return cache.getAnimalsWithBreed().map { animalList -> animalList.map { it.toAnimalDomain() } }
    }

    override fun getAnimalsWithBreedListFromDb(): List<Animal> {
        return cache.getAnimalsWithBreedList().map { it.toAnimalDomain() }
    }

    override suspend fun deleteAllAnimalsWithBreedCategories() {
        cache.deleteAllAnimalsWithBreedCategories()
    }

    override suspend fun deleteAllAnimalsWithCategories() {
        cache.deleteAllAnimalsWithCategories()
    }

    private var latestAnimalIds: List<String> = emptyList()
    private var latestAnimalIdsRegular: List<String> = emptyList()

    override suspend fun requestMoreAnimalsFromAPI(
        pageToLoad: Int,
        pageSize: Int,
        categIds: List<String>,
        breedIds: List<String>,
        hasBreeds: Boolean
    ): PaginatedAnimals {
        try {
            val response: Response<List<ApiAnimal>> = api.getAnimals(
                pageToLoad = pageToLoad,
                pageSize = pageSize,
                categIds = categIds.joinToString(","),
                breedIds = breedIds.joinToString(","),
                hasBreeds = hasBreeds
            )

            val headers = response.headers().toMultimap()
            val totalCount = headers["pagination-count"]?.get(0)?.toIntOrNull() ?: 0
            val countPerPage = headers["pagination-limit"]?.get(0)?.toIntOrNull() ?: 1
            val currentPage = headers["pagination-page"]?.get(0)?.toIntOrNull() ?: 0
            val totalPages = totalCount / countPerPage

            val animals = response.body() ?: emptyList()

            // Check if the animal IDs are the same as the last request
            val currentAnimalIds = animals.map { it.id }.filterNotNull()
            if (currentAnimalIds == latestAnimalIdsRegular) {
                return PaginatedAnimals(
                    emptyList(),
                    Pagination(
                        currentPage = currentPage,
                        totalPages = totalPages
                    )
                )
            }
            latestAnimalIdsRegular = currentAnimalIds

            return PaginatedAnimals(
                animals.map { apiAnimalMapper.mapToDomain(it) },
                Pagination(
                    currentPage = currentPage,
                    totalPages = totalPages
                )
            )
        } catch (exception: HttpException) {
            throw NetworkException
        }
    }

    override suspend fun requestMoreAnimalsByBreedFromAPI(
        pageToLoad: Int,
        pageSize: Int,
        breedIds: List<String>,
    ): PaginatedAnimals {
        try {
            val response: Response<List<ApiAnimal>> = api.getAnimalsByBreed(
                pageToLoad = pageToLoad,
                pageSize = pageSize,
                breedIds = breedIds.joinToString(","),
            )

            val headers = response.headers().toMultimap()
            val totalCount = headers["pagination-count"]?.get(0)?.toIntOrNull() ?: 0
            val countPerPage = headers["pagination-limit"]?.get(0)?.toIntOrNull() ?: 1
            val currentPage = headers["pagination-page"]?.get(0)?.toIntOrNull() ?: 0
            val totalPages = totalCount / countPerPage

            val animals = response.body() ?: emptyList()

            // Check if the animal IDs are the same as the last request
            val currentAnimalIds = animals.map { it.id }.filterNotNull()
            if (currentAnimalIds == latestAnimalIds) {
                return PaginatedAnimals(
                    emptyList(),
                    Pagination(
                        currentPage = currentPage,
                        totalPages = totalPages
                    )
                )
            }
            latestAnimalIds = currentAnimalIds

            return PaginatedAnimals(
                animals.map { apiAnimalMapper.mapToDomain(it) },
                Pagination(
                    currentPage = currentPage,
                    totalPages = totalPages
                )
            )
        } catch (exception: HttpException) {
            throw NetworkException
        }
    }

    override suspend fun storeAnimalsInDb(animals: List<Animal>, isWithCategories: Boolean, isWithBreed: Boolean) {
        cache.storeNearbyAnimals(animals.map { CachedAnimalAggregate.fromDomain(it, isWithCategories, isWithBreed) })
    }

    override suspend fun getAnimalFromDb(id: String): Animal? {
        val cachedAnimal = cache.getAnimalById(id)
        return cachedAnimal?.toAnimalDomain()
    }

}