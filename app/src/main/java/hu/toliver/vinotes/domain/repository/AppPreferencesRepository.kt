package hu.toliver.vinotes.domain.repository

import kotlinx.coroutines.flow.Flow
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.model.enums.ThemeMode

interface AppPreferencesRepository {
    val catalogUrl: Flow<String>
    val welcomeShown: Flow<Boolean>
    val username: Flow<String>
    val themeMode: Flow<ThemeMode>
    val appLanguage: Flow<AppLanguage>

    suspend fun saveCatalogUrl(url: String)
    suspend fun saveWelcomeShown(shown: Boolean)
    suspend fun saveUsername(name: String)
    suspend fun saveThemeMode(mode: ThemeMode)
    suspend fun saveAppLanguage(language: AppLanguage)
    suspend fun resetUsername()
    suspend fun clearAllData()
}