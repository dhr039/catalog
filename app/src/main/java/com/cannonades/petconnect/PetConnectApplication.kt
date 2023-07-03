package com.cannonades.petconnect

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class PetConnectApplication : Application(), ImageLoaderFactory {
    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun newImageLoader(): ImageLoader = imageLoader.get()
}