package hu.toliver.vinotes.ui.screen.winelist

import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.WineWithStats
import hu.toliver.vinotes.domain.model.enums.WineColour

data class WineListState(
    val isLoading: Boolean = true,
    val wines: List<WineWithStats> = emptyList(),
    val searchQuery: String = "",
    val activeFilters: WineFilters = WineFilters(),
    val sortOrder: WineSortOrder = WineSortOrder.NAME_ASC,
    val isFilterSheetOpen: Boolean = false,
    val editingWine: Wine? = null,
    val isAddSheetOpen: Boolean = false,
    val errorMessage: String? = null,
)

data class WineFilters(
    val colours: Set<WineColour> = emptySet(),
    val countries: Set<String> = emptySet(),
    val yearFrom: Int? = null,
    val yearTo: Int? = null,
) {
    val isActive: Boolean
        get() = colours.isNotEmpty() || countries.isNotEmpty()
                || yearFrom != null || yearTo != null
}

enum class WineSortOrder {
    NAME_ASC,
    RATING_DESC,
    YEAR_DESC,
    TASTING_DATE_DESC,
}

