package hu.toliver.vinotes.ui.screen.settings

sealed interface SettingsEvent {
    data object LoadData : SettingsEvent

    data object EditUrlClicked : SettingsEvent
    data class UrlEditChanged(val value: String) : SettingsEvent
    data object UrlEditConfirmed : SettingsEvent
    data object UrlEditDismissed : SettingsEvent
    data object UrlResetToDefault : SettingsEvent

    data object EditUsernameClicked : SettingsEvent
    data class UsernameEditChanged(val value: String) : SettingsEvent
    data object UsernameEditConfirmed : SettingsEvent
    data object UsernameEditDismissed : SettingsEvent

    data object ClearDataClicked : SettingsEvent
    data object ClearDataConfirmed : SettingsEvent
    data object ClearDataDismissed : SettingsEvent

    data object ImportFromFileClicked : SettingsEvent
    data object UpdateFromWebClicked : SettingsEvent

    data object AboutInfoClicked : SettingsEvent
    data object AboutInfoDismissed : SettingsEvent
}

