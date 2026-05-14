package hu.toliver.vinotes.domain.model

import java.util.Date

data class WineWithStats(
    val wine: Wine,
    val tastingCount: Int,           // összes kóstolás száma ehhez a borhoz
    val latestRating: Int?,          // legutóbbi kóstolás rating-je, null ha nincs
    val latestTastingDate: Date?,    // java.util.Date, null ha nincs kóstolás
)

