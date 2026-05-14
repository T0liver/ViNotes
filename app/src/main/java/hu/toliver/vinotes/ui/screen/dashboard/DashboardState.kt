package hu.toliver.vinotes.ui.screen.dashboard

data class DashboardState(
    val isLoading: Boolean = true,
    val recentTastings: List<RecentTastingItem> = emptyList(),

    val totalWines: Int = 0,
    val totalTastings: Int = 0,
    val averageRating: Double = 0.0,
    val topRegion: String = "",

    val errorMessage: String? = null,
)

data class RecentTastingItem(
    val tasteId: String,
    val wineId: String,
    val wineName: String,
    val producer: String,
    val year: Int,
    val rating: Int,
    val date: String,
    val colourHex: String,
)