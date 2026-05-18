package hu.toliver.vinotes.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import hu.toliver.vinotes.data.local.preferences.AppPreferencesKeys
import hu.toliver.vinotes.ui.AppConstants
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import hu.toliver.vinotes.domain.model.enums.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AppPreferencesRepository {

    override val catalogUrl: Flow<String> = dataStore.data
        .map { prefs -> prefs[AppPreferencesKeys.CATALOG_URL] ?: AppConstants.DEFAULT_CATALOG_URL }
        .catch { emit(AppConstants.DEFAULT_CATALOG_URL) }

    override val username: Flow<String> = dataStore.data
        .map { prefs -> prefs[AppPreferencesKeys.USERNAME] ?: "" }
        .catch { emit(AppConstants.APP_AUTHOR) }

    override val themeMode: Flow<ThemeMode> = dataStore.data
        .map { prefs ->
            when (prefs[AppPreferencesKeys.THEME_MODE]) {
                ThemeMode.LIGHT.name -> ThemeMode.LIGHT
                ThemeMode.DARK.name -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        }
        .catch { emit(ThemeMode.SYSTEM) }

    override val appLanguage: Flow<AppLanguage> = dataStore.data
        .map { prefs ->
            when (prefs[AppPreferencesKeys.APP_LANGUAGE]) {
                AppLanguage.ENGLISH.name -> AppLanguage.ENGLISH
                AppLanguage.HUNGARIAN.name -> AppLanguage.HUNGARIAN
                else -> AppLanguage.SYSTEM
            }
        }
        .catch { emit(AppLanguage.SYSTEM) }

    override suspend fun saveCatalogUrl(url: String) {
        dataStore.edit { prefs -> prefs[AppPreferencesKeys.CATALOG_URL] = url }
    }

    override suspend fun saveUsername(name: String) {
        dataStore.edit { prefs -> prefs[AppPreferencesKeys.USERNAME] = name }
    }

    override suspend fun saveThemeMode(mode: ThemeMode) {
        dataStore.edit { prefs -> prefs[AppPreferencesKeys.THEME_MODE] = mode.name }
    }

    override suspend fun saveAppLanguage(language: AppLanguage) {
        dataStore.edit { prefs -> prefs[AppPreferencesKeys.APP_LANGUAGE] = language.name }
    }

    override suspend fun resetUsername() {
        dataStore.edit { prefs -> prefs.remove(AppPreferencesKeys.USERNAME) }
    }

    override suspend fun clearAllData() {
        resetUsername()
        dataStore.edit {
            prefs -> prefs[AppPreferencesKeys.CATALOG_URL] = AppConstants.DEFAULT_CATALOG_URL
        }
    }
}