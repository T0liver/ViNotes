package hu.toliver.vinotes.domain.model.sync

import kotlinx.datetime.LocalDate

data class CatalogManifest(
    val schemaVersion: Int,
    val lastUpdated: LocalDate,
    val full: FullCatalogInfo,
    val deltas: List<DeltaInfo>
)