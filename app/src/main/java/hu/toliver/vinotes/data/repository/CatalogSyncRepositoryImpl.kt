package hu.toliver.vinotes.data.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.data.dao.SyncMetadataDao
import hu.toliver.vinotes.data.dao.WineDao
import hu.toliver.vinotes.data.local.entity.toDomain
import hu.toliver.vinotes.data.local.entity.toEntity
import hu.toliver.vinotes.data.remote.api.CatalogApi
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
import kotlinx.serialization.json.Json

class CatalogSyncRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val wineDao: WineDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val catalogApi: CatalogApi
) : CatalogSyncRepository {

    override fun getSyncMetadata(): Flow<SyncMetadata?> =
        syncMetadataDao.get().map { it?.toDomain() }

    override suspend fun updateSyncMetadata(metadata: SyncMetadata): Result<Unit> = runCatching {
        syncMetadataDao.upsert(metadata.toEntity())
    }

    override suspend fun fetchManifest(): Result<CatalogManifest> = runCatching {
        catalogApi.getManifest().toDomain()
    }

    override suspend fun downloadFull(): Result<List<Wine>> = runCatching {
        catalogApi.getFullCatalog().toDomain()
    }

    override suspend fun downloadDelta(deltaInfo: DeltaInfo): Result<CatalogDelta> = runCatching {
        catalogApi.getDelta(deltaInfo.url).toDomain()
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