package hu.toliver.vinotes.ui.screen.dashboard

sealed interface DashboardEvent {
    data object LoadData : DashboardEvent
    data object RefreshRequested : DashboardEvent
    data class TastingCardClicked(val tasteId: String) : DashboardEvent
    data object AddTastingClicked : DashboardEvent
    data object SeeAllTastingsClicked : DashboardEvent
    data object ErrorDismissed : DashboardEvent
    data object WelcomeDialogConfirmed : DashboardEvent
}