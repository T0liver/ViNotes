package hu.toliver.vinotes.ui.screen.winelist

import hu.toliver.vinotes.domain.model.Wine

sealed interface WineListEvent {
    data object LoadData : WineListEvent
    data class SearchQueryChanged(val query: String) : WineListEvent
    data class SortOrderChanged(val order: WineSortOrder) : WineListEvent
    data object FilterSheetOpened : WineListEvent
    data object FilterSheetDismissed : WineListEvent
    data class FiltersApplied(val filters: WineFilters) : WineListEvent
    data object FiltersCleared : WineListEvent
    data object AddWineClicked : WineListEvent
    data object AddSheetDismissed : WineListEvent
    data class WineCardClicked(val wineId: String) : WineListEvent
    data class WineCardLongPressed(val wine: Wine) : WineListEvent
    data object EditSheetDismissed : WineListEvent
    data class WineSaved(val wine: Wine, val isNew: Boolean) : WineListEvent
    data class WineDeleteRequested(val wine: Wine) : WineListEvent
    data class WineDeleteConfirmed(val wine: Wine) : WineListEvent
}

