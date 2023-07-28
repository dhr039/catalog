package com.cannonades.petconnect.feature.settings.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cannonades.petconnect.feature.settings.DARK_THEME_KEY
import com.cannonades.petconnect.feature.settings.FOLLOW_SYSTEM_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private val _darkThemeConfig = MutableStateFlow(DarkThemeConfig.FOLLOW_SYSTEM)
    val darkThemeConfig: StateFlow<DarkThemeConfig> = _darkThemeConfig

    init {
        viewModelScope.launch {
            _darkThemeConfig.value = loadDarkThemeConfig()
        }
    }

    private suspend fun loadDarkThemeConfig(): DarkThemeConfig {
        return dataStore.data.firstOrNull()?.let { preferences ->
            when {
                preferences[DARK_THEME_KEY] == true -> DarkThemeConfig.DARK
                preferences[DARK_THEME_KEY] == false && preferences[FOLLOW_SYSTEM_KEY] == true -> DarkThemeConfig.FOLLOW_SYSTEM
                preferences[DARK_THEME_KEY] == false && preferences[FOLLOW_SYSTEM_KEY] == false -> DarkThemeConfig.LIGHT
                else -> DarkThemeConfig.FOLLOW_SYSTEM // Default if preferences don't exist
            }
        } ?: DarkThemeConfig.FOLLOW_SYSTEM // Default if dataStore is empty
    }

    suspend fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        when(darkThemeConfig) {
            DarkThemeConfig.FOLLOW_SYSTEM -> {
                dataStore.edit { preferences ->
                    preferences[DARK_THEME_KEY] = false
                    preferences[FOLLOW_SYSTEM_KEY] = true
                }
                _darkThemeConfig.value = DarkThemeConfig.FOLLOW_SYSTEM
            }
            DarkThemeConfig.LIGHT -> {
                dataStore.edit { preferences ->
                    preferences[DARK_THEME_KEY] = false
                    preferences[FOLLOW_SYSTEM_KEY] = false
                }
                _darkThemeConfig.value = DarkThemeConfig.LIGHT
            }
            DarkThemeConfig.DARK -> {
                dataStore.edit { preferences ->
                    preferences[DARK_THEME_KEY] = true
                    preferences[FOLLOW_SYSTEM_KEY] = false
                }
                _darkThemeConfig.value = DarkThemeConfig.DARK
            }
        }
    }
}
