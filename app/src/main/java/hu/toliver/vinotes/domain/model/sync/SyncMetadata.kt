package hu.toliver.vinotes.domain.model.sync

import kotlinx.datetime.LocalDate

data class SyncMetadata(
    val lastSyncDate: LocalDate?,
    val lastAppliedDelta: LocalDate?,
    val totalWinesInCatalog: Int,
    val isFullImportDone: Boolean
)