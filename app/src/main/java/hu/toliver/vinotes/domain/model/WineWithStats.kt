package hu.toliver.vinotes.domain.model

import java.util.Date

data class WineWithStats(
    val wine: Wine,
    val tastingCount: Int,
    val latestRating: Int?,
    val latestTastingDate: Date?,
)