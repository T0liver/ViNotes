package hu.toliver.vinotes.domain.model

data class DashboardStats(
    val totalWines: Int,
    val totalTastings: Int,
    val averageRating: Double,
    val topRegion: String,
)

