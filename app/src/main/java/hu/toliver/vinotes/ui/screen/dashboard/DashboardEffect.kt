package hu.toliver.vinotes.ui.screen.dashboard

sealed interface DashboardEffect {
    data class NavigateToDetail(val wineId: String, val tasteId: String) : DashboardEffect
    data object NavigateToAddTasting   : DashboardEffect
    data object NavigateToWineList     : DashboardEffect
    data class  ShowError(val message: String)                  : DashboardEffect
}