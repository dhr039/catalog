package com.cannonades.petconnect

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetConnectApplication : Application() {
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
}