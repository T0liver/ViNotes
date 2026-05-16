package hu.toliver.vinotes.data.local.preferences

import androidx.datastore.preferences.core.stringPreferencesKey

object AppPreferencesKeys {
    val CATALOG_URL = stringPreferencesKey("catalog_url")
    val USERNAME = stringPreferencesKey("username")
}