package hu.toliver.vinotes.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.data.dao.SyncMetadataDao
import hu.toliver.vinotes.data.dao.WineDao
import hu.toliver.vinotes.data.local.entity.toDomain
import hu.toliver.vinotes.data.local.entity.toEntity
import hu.toliver.vinotes.data.remote.api.CatalogApi
import hu.toliver.vinotes.domain.repository.AppPreferencesRepository
import hu.toliver.vinotes.data.remote.dto.FullCatalogDto
import hu.toliver.vinotes.data.remote.mapper.toDomain
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.sync.CatalogDelta
import hu.toliver.vinotes.domain.model.sync.CatalogManifest
import hu.toliver.vinotes.domain.model.sync.DeltaInfo
import hu.toliver.vinotes.domain.model.sync.SyncMetadata
import hu.toliver.vinotes.domain.repository.CatalogSyncRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import hu.toliver.vinotes.ui.AppConstants
import java.net.UnknownHostException

class CatalogSyncRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val wineDao: WineDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val catalogApi: CatalogApi,
    private val appPreferencesRepository: AppPreferencesRepository
) : CatalogSyncRepository {

    override fun getSyncMetadata(): Flow<SyncMetadata?> =
        syncMetadataDao.get().map { it?.toDomain() }

    override suspend fun updateSyncMetadata(metadata: SyncMetadata): Result<Unit> = runCatching {
        syncMetadataDao.upsert(metadata.toEntity())
    }

    override suspend fun fetchManifest(): Result<CatalogManifest> = runCatching {
        val rawBase = appPreferencesRepository.catalogUrl.firstOrNull() ?: AppConstants.DEFAULT_CATALOG_URL
        val base = if (rawBase.startsWith("http://") || rawBase.startsWith("https://")) rawBase else "https://$rawBase"
        val url = base.trimEnd('/') + "/manifest.json"
        try {
            catalogApi.getManifest(url).toDomain()
        } catch (e: UnknownHostException) {
            throw Exception("DNS resolution failed for host when fetching manifest: ${e.message}", e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun downloadFull(): Result<List<Wine>> = runCatching {
        val rawBase = appPreferencesRepository.catalogUrl.firstOrNull() ?: AppConstants.DEFAULT_CATALOG_URL
        val base = if (rawBase.startsWith("http://") || rawBase.startsWith("https://")) rawBase else "https://$rawBase"
        val url = base.trimEnd('/') + "/full.json"
        try {
            catalogApi.getFullCatalog(url).toDomain()
        } catch (e: UnknownHostException) {
            throw Exception("DNS resolution failed for host when downloading full catalog: ${e.message}", e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun downloadDelta(deltaInfo: DeltaInfo): Result<CatalogDelta> = runCatching {
        // deltaInfo.url is expected to be a relative filename (e.g. "delta-2025-05-16.json")
        val relative = deltaInfo.url
        val rawBase = appPreferencesRepository.catalogUrl.firstOrNull() ?: AppConstants.DEFAULT_CATALOG_URL
        val base = if (rawBase.startsWith("http://") || rawBase.startsWith("https://")) rawBase else "https://$rawBase"
        val fullUrl = if (relative.startsWith("http://") || relative.startsWith("https://")) {
            relative
        } else {
            base.trimEnd('/') + "/" + relative.trimStart('/')
        }
        try {
            catalogApi.getDelta(fullUrl).toDomain()
        } catch (e: UnknownHostException) {
            throw Exception("DNS resolution failed for host when downloading delta: ${e.message}", e)
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun importFromLocalFile(uri: Uri): Result<List<Wine>> = runCatching {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("Could not open file: $uri")
        val content = inputStream.bufferedReader().use { it.readText() }
        val fullCatalogDto = json.decodeFromString<FullCatalogDto>(content)
        fullCatalogDto.toDomain()
    }
}