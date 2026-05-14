package hu.toliver.vinotes.ui.screen.winelist

sealed interface WineListEffect {
    data class NavigateToDetail(val wineId: String) : WineListEffect
    data class ShowSnackbar(val message: String) : WineListEffect
}

