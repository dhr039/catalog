package com.cannonades.petconnect.common.data.di

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.cannonades.petconnect.common.data.api.ApiConstants
import com.cannonades.petconnect.common.data.api.PetFaceApi
import com.cannonades.petconnect.common.data.api.interceptors.LoggingInterceptor
import com.cannonades.petconnect.common.data.api.interceptors.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideApi(builder: Retrofit.Builder): PetFaceApi {
        return builder
            .build()
            .create(PetFaceApi::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_ENDPOINT)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        networkStatusInterceptor: NetworkStatusInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    fun provideHttpLoggingInterceptor(loggingInterceptor: LoggingInterceptor): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(loggingInterceptor)

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }

    /**
     * coil stuff. If you need coil debug logging see an example in the
     * nowinadroid project, NetworkModule.kt
     * */
    @Provides
    @Singleton
    fun imageLoader(
        @ApplicationContext application: Context,
    ): ImageLoader = ImageLoader.Builder(application)
        // Assume most content images are versioned urls
        // but some problematic images are fetching each time
        .respectCacheHeaders(false)
        .memoryCache {
            MemoryCache.Builder(application)
                .maxSizePercent(0.5) //default is 0.25
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(application.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.7) //default is 0.02
                .build()
        }
        .build()
}