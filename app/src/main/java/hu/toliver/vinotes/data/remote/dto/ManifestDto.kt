package hu.toliver.vinotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ManifestDto(
    val schemaVersion: Int,
    val lastUpdated: String,
    val full: FullCatalogInfoDto,
    val deltas: List<DeltaInfoDto>
)

@Serializable
data class FullCatalogInfoDto(
    val url: String,
    val wineCount: Int,
    val sha256: String
)

@Serializable
data class DeltaInfoDto(
    val date: String,
    val url: String,
    val addedCount: Int,
    val sha256: String
)