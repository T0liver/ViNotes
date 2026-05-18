package hu.toliver.vinotes.ui.screen.settings

import hu.toliver.vinotes.ui.AppConstants
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.model.enums.ThemeMode

data class SettingsState(
    val isLoading: Boolean = true,
    val catalogUrl: String = AppConstants.DEFAULT_CATALOG_URL,
    val username: String = AppConstants.APP_AUTHOR,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val appLanguage: AppLanguage = AppLanguage.SYSTEM,
    val showUrlEditDialog: Boolean = false,
    val showUsernameEditDialog: Boolean = false,
    val showThemeDialog: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val editingUrlValue: String = "",
    val editingUsernameValue: String = "",
    val showClearDataDialog: Boolean = false,
    val showAboutInfoDialog: Boolean = false,
    val isClearingData: Boolean = false,
    val isSyncingCatalog: Boolean = false,
    val isImportingCatalog: Boolean = false,
)
