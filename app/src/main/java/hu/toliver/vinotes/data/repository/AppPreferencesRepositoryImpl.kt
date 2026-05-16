package hu.toliver.vinotes.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import hu.toliver.vinotes.data.local.preferences.AppPreferencesKeys
import hu.toliver.vinotes.ui.AppConstants
import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class AppPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AppPreferencesRepository {

    override val catalogUrl: Flow<String> = dataStore.data
        .map { prefs -> prefs[AppPreferencesKeys.CATALOG_URL] ?: AppConstants.DEFAULT_CATALOG_URL }
        .catch { emit(AppConstants.DEFAULT_CATALOG_URL) }

    override val username: Flow<String> = dataStore.data
        .map { prefs -> prefs[AppPreferencesKeys.USERNAME] ?: "" }
        .catch { emit(AppConstants.APP_AUTHOR) }

    override suspend fun saveCatalogUrl(url: String) {
        dataStore.edit { prefs -> prefs[AppPreferencesKeys.CATALOG_URL] = url }
    }

    override suspend fun saveUsername(name: String) {
        dataStore.edit { prefs -> prefs[AppPreferencesKeys.USERNAME] = name }
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