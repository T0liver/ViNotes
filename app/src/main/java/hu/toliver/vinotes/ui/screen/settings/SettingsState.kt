package hu.toliver.vinotes.ui.screen.settings

import hu.toliver.vinotes.ui.AppConstants

data class SettingsState(
    val isLoading: Boolean = true,
    val catalogUrl: String = AppConstants.DEFAULT_CATALOG_URL,
    val username: String = AppConstants.APP_AUTHOR,
    val showUrlEditDialog: Boolean = false,
    val showUsernameEditDialog: Boolean = false,
    val editingUrlValue: String = "",
    val editingUsernameValue: String = "",
    val showClearDataDialog: Boolean = false,
    val showAboutInfoDialog: Boolean = false,
    val isClearingData: Boolean = false,
    val isSyncingCatalog: Boolean = false,
)

