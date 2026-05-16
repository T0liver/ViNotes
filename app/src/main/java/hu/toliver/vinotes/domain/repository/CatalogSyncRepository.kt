package hu.toliver.vinotes.domain.repository

import android.net.Uri
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.sync.CatalogDelta
import hu.toliver.vinotes.domain.model.sync.CatalogManifest
import hu.toliver.vinotes.domain.model.sync.DeltaInfo
import hu.toliver.vinotes.domain.model.sync.SyncMetadata
import kotlinx.coroutines.flow.Flow

interface CatalogSyncRepository {
    suspend fun fetchManifest(): Result<CatalogManifest>
    suspend fun updateSyncMetadata(metadata: SyncMetadata): Result<Unit>
    suspend fun downloadFull(): Result<List<Wine>>
    suspend fun downloadDelta(deltaInfo: DeltaInfo): Result<CatalogDelta>
    suspend fun importFromLocalFile(uri: Uri): Result<List<Wine>>
    fun getSyncMetadata(): Flow<SyncMetadata?>
}