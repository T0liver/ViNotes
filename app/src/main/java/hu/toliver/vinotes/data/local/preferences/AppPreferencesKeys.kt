package hu.toliver.vinotes.data.local.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object AppPreferencesKeys {
    val CATALOG_URL = stringPreferencesKey("catalog_url")
    val USERNAME = stringPreferencesKey("username")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val APP_LANGUAGE = stringPreferencesKey("app_language")
}