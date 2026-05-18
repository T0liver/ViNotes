package hu.toliver.vinotes.ui.screen.settings

sealed interface SettingsEffect {
    data object NavigateUp : SettingsEffect
    data class ShowSnackbar(val message: String) : SettingsEffect
    data class OpenUrl(val url: String) : SettingsEffect
    data object OpenImportFilePicker : SettingsEffect
}

