package hu.toliver.vinotes.ui.screen.addtasting

sealed interface AddTastingEffect {
    data object NavigateUp : AddTastingEffect
    data class ShowSnackbar(val message: String) : AddTastingEffect
    data object RequestLocationPermission : AddTastingEffect
    data class ShowLocationError(val message: String) : AddTastingEffect
}
