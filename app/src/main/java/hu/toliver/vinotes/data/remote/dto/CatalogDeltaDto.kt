package hu.toliver.vinotes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CatalogDeltaDto(
    val type: String,
    val date: String,
    val added: List<WineDto>,
    val updated: List<WineDto>,
    val removedIds: List<String>
)