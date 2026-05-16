package hu.toliver.vinotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FullCatalogDto(
    val type: String,
    val exportedAt: String,
    val wines: List<WineDto>
)