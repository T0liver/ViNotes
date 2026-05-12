package hu.toliver.winotes.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.toliver.winotes.domain.model.sync.SyncMetadata
import kotlinx.datetime.LocalDate

@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity (
    @PrimaryKey val id: Int = 1,
    val lastSyncDate: Long?,
    val lastAppliedDelta: String?,
    val totalWinesInCatalog: Int,
    val isFullImportDone: Boolean
)

fun SyncMetadataEntity.toDomain(): SyncMetadata = SyncMetadata(
    lastSyncDate = lastSyncDate?.let { LocalDate.fromEpochDays(it.toInt()) },
    lastAppliedDelta = lastAppliedDelta?.let { LocalDate.parse(it) },
    totalWinesInCatalog = totalWinesInCatalog,
    isFullImportDone = isFullImportDone
)

fun SyncMetadata.toEntity(): SyncMetadataEntity = SyncMetadataEntity(
    id = 1,
    lastSyncDate = lastSyncDate?.toEpochDays(),
    lastAppliedDelta = lastAppliedDelta?.toString(),
    totalWinesInCatalog = totalWinesInCatalog,
    isFullImportDone = isFullImportDone
)