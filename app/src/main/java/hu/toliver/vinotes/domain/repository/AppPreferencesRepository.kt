package hu.toliver.vinotes.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    val catalogUrl: Flow<String>
    val username: Flow<String>

    suspend fun saveCatalogUrl(url: String)
    suspend fun saveUsername(name: String)
    suspend fun resetUsername()
}