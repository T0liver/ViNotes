package hu.toliver.vinotes.ui.screen.winedetail

import hu.toliver.vinotes.domain.model.Wine

sealed interface WineDetailEvent {
    data object LoadData : WineDetailEvent
    data object EditWineClicked : WineDetailEvent
    data object EditSheetDismissed : WineDetailEvent
    data class WineSaved(val wine: Wine) : WineDetailEvent
    data object DeleteWineClicked : WineDetailEvent
    data object DeleteWineConfirmed : WineDetailEvent
    data object DeleteWineDismissed : WineDetailEvent
    data object AddTastingClicked : WineDetailEvent
    data class TastingClicked(val tasteId: String) : WineDetailEvent  // TODO: later
}

