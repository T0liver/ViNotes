package hu.toliver.vinotes.domain.model.sync

import kotlinx.datetime.LocalDate

data class DeltaInfo(
    val date: LocalDate,
    val url: String,
    val addedCount: Int,
    val sha256: String
)