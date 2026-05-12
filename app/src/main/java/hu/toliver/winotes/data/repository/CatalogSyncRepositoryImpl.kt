package hu.toliver.winotes.data.repository

import android.net.Uri
import hu.toliver.winotes.data.dao.SyncMetadataDao
import hu.toliver.winotes.data.dao.WineDao
import hu.toliver.winotes.data.local.entity.toDomain
import hu.toliver.winotes.data.local.entity.toEntity
import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.model.sync.CatalogManifest
import hu.toliver.winotes.domain.model.sync.DeltaInfo
import hu.toliver.winotes.domain.model.sync.SyncMetadata
import hu.toliver.winotes.domain.repository.CatalogSyncRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CatalogSyncRepositoryImpl @Inject constructor(
    private val wineDao: WineDao,
    private val syncMetadataDao: SyncMetadataDao,
    // private val catalogApi: CatalogApi  ← majd Retrofit után
) : CatalogSyncRepository {

    override fun getSyncMetadata(): Flow<SyncMetadata?> =
        syncMetadataDao.get().map { it?.toDomain() }

    override suspend fun updateSyncMetadata(metadata: SyncMetadata): Result<Unit> = runCatching {
        syncMetadataDao.upsert(metadata.toEntity())
    }

    override suspend fun fetchManifest(): Result<CatalogManifest> {
        TODO("Retrofit layer is not implemented yet")
    }

    override suspend fun downloadFull(): Result<List<Wine>> {
        TODO("Retrofit layer is not implemented yet")
    }

    override suspend fun downloadDelta(deltaInfo: DeltaInfo): Result<List<Wine>> {
        TODO("Retrofit layer is not implemented yet")
    }

    override suspend fun importFromLocalFile(uri: Uri): Result<List<Wine>> {
        TODO("Not yet implemented")
    }
}