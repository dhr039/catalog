package com.cannonades.petconnect.settings.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import com.cannonades.petconnect.settings.CATEGORIES_KEY
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MyArrayViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val moshi = Moshi.Builder().build()
    private val arrayAdapter: JsonAdapter<Array<Int>> = moshi.adapter(Array<Int>::class.java)

    val array: Flow<Array<Int>> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            val arrayAsString = preferences[CATEGORIES_KEY] ?: ""
            if(arrayAsString.isEmpty()) {
                arrayOf()
            } else {
                val result = arrayAdapter.fromJson(arrayAsString)
                result as? Array<Int> ?: arrayOf()
            }
        }
        .distinctUntilChanged()

    suspend fun updateArray(array: Array<Int>) {
        dataStore.edit { preferences ->
            preferences[CATEGORIES_KEY] = arrayAdapter.toJson(array)
        }
    }
}