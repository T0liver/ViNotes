package hu.toliver.vinotes.ui.screen.tastingdetail

sealed interface TastingDetailEffect {
    data object NavigateUp : TastingDetailEffect
    data class ShowSnackbar(val message: String) : TastingDetailEffect
}