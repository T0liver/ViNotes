package hu.toliver.vinotes.domain.repository

import kotlinx.coroutines.flow.Flow
import hu.toliver.vinotes.domain.model.enums.ThemeMode

interface AppPreferencesRepository {
    val catalogUrl: Flow<String>
    val username: Flow<String>
    val themeMode: Flow<ThemeMode>

    suspend fun saveCatalogUrl(url: String)
    suspend fun saveUsername(name: String)
    suspend fun saveThemeMode(mode: ThemeMode)
    suspend fun resetUsername()
    suspend fun clearAllData()
}