package com.cannonades.petconnect.feature.settings.presentation

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.ViewModel
import com.cannonades.petconnect.feature.settings.DARK_THEME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    val isDarkTheme: Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }
        .distinctUntilChanged()

    suspend fun updateDarkThemeSetting(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }

    private val _darkThemeConfig = MutableStateFlow<DarkThemeConfig>(DarkThemeConfig.FOLLOW_SYSTEM)
    val darkThemeConfig: StateFlow<DarkThemeConfig> = _darkThemeConfig


    suspend fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        when(darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> {
                Log.i("SettingsViewModel", "FOLLOW_SYSTEM")
                _darkThemeConfig.value = DarkThemeConfig.FOLLOW_SYSTEM
            }
            DarkThemeConfig.LIGHT -> {
                Log.i("SettingsViewModel", "LIGHT")
                _darkThemeConfig.value = DarkThemeConfig.LIGHT
//                dataStore.edit { preferences ->
//                    preferences[DARK_THEME_KEY] = false
//                }
            }
            DarkThemeConfig.DARK -> {
                Log.i("SettingsViewModel", "DARK")
                _darkThemeConfig.value = DarkThemeConfig.DARK
//                dataStore.edit { preferences ->
//                    preferences[DARK_THEME_KEY] = true
//                }
            }
        }
//        viewModelScope.launch {
//            userDataRepository.setDarkThemeConfig(darkThemeConfig)
//        }
    }
}
