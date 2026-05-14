package hu.toliver.vinotes.ui.screen.winedetail

sealed interface WineDetailEffect {
    data object NavigateUp : WineDetailEffect
    data class NavigateToAddTasting(val wineId: String) : WineDetailEffect
    data class ShowSnackbar(val message: String) : WineDetailEffect
}

