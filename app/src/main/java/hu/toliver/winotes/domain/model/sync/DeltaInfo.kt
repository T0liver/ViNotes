package hu.toliver.winotes.domain.model.sync

import kotlinx.datetime.LocalDate

data class DeltaInfo(
    val date: LocalDate,
    val url: String,
    val addedCount: Int,
    val sha256: String
)