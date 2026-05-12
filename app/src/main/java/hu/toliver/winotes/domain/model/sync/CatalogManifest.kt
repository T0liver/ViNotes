package hu.toliver.winotes.domain.model.sync

import kotlinx.datetime.LocalDate

data class CatalogManifest(
    val schemaVersion: Int,
    val lastUpdated: LocalDate,
    val full: FullCatalogInfo,
    val deltas: List<DeltaInfo>
)