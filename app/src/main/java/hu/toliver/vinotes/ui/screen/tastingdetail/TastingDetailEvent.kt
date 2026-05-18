package hu.toliver.vinotes.ui.screen.tastingdetail

sealed interface TastingDetailEvent {
    data object LoadData : TastingDetailEvent
    data object DeleteTastingClicked : TastingDetailEvent
    data object DeleteTastingConfirmed : TastingDetailEvent
    data object DeleteTastingDismissed : TastingDetailEvent
}