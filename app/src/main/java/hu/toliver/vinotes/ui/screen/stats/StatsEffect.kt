package hu.toliver.vinotes.ui.screen.stats

sealed interface StatsEffect {
    data class ShowSnackbar(val message: String) : StatsEffect
}

