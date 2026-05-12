package hu.toliver.winotes.domain.repository

import android.net.Uri
import hu.toliver.winotes.domain.model.Wine
import hu.toliver.winotes.domain.model.sync.CatalogManifest
import hu.toliver.winotes.domain.model.sync.DeltaInfo
import hu.toliver.winotes.domain.model.sync.SyncMetadata
import kotlinx.coroutines.flow.Flow

interface CatalogSyncRepository {
    suspend fun fetchManifest(): Result<CatalogManifest>
    suspend fun updateSyncMetadata(metadata: SyncMetadata): Result<Unit>
    suspend fun downloadFull(): Result<List<Wine>>
    suspend fun downloadDelta(deltaInfo: DeltaInfo): Result<List<Wine>>
    suspend fun importFromLocalFile(uri: Uri): Result<List<Wine>>
    fun getSyncMetadata(): Flow<SyncMetadata?>
}