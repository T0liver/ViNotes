package hu.toliver.vinotes.domain.model.sync

import hu.toliver.vinotes.domain.model.Wine

data class CatalogDelta(
    val date: kotlinx.datetime.LocalDate,
    val added: List<Wine>,
    val updated: List<Wine>,
    val removedIds: List<String>
)