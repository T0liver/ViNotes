package hu.toliver.vinotes.ui.screen.dashboard

sealed interface DashboardEffect {
    data class NavigateToTastingDetail(val tasteId: String) : DashboardEffect
    data class NavigateToWineDetail(val wineId: String) : DashboardEffect
    data object NavigateToAddTasting : DashboardEffect
    data object NavigateToWineList : DashboardEffect
    data class  ShowError(val message: String) : DashboardEffect
}