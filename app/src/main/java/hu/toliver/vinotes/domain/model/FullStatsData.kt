package hu.toliver.vinotes.domain.model

import hu.toliver.vinotes.domain.model.enums.WineColour

data class FullStatsData(
    val totalWines:        Int,
    val totalTastings:     Int,
    val averageRating:     Double,
    val wouldDrinkAgainPct: Int,

    val currentStreak:  Int,
    val longestStreak:  Int,

    // Key: "yyyy-MM-dd"
    val tastingsByDay: Map<String, Int>,

    // Key: lane's lower bound
    val ratingBuckets: Map<Int, Int>,

    val colourDistribution: Map<WineColour, Int>,

    // Top regions (max 6, descending order) ───────────────────────────────
    val topRegions: List<Pair<String, Int>>,

    val vintageDistribution: Map<Int, Int>,

    // ── Időszak-specifikus ───────────────────────────────────────────────────
    val tastingsInPeriod: Int,
    val avgRatingInPeriod: Double,
)

