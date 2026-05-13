package hu.toliver.vinotes.ui.navigation

import kotlinx.serialization.Serializable

sealed interface NavKey {

    // ── Bottom nav gyökér képernyők ───────────────────────────────────────────
    @Serializable data object Dashboard  : NavKey
    @Serializable data object WineList   : NavKey
    @Serializable data object Stats      : NavKey

    // ── Detail / flow képernyők ───────────────────────────────────────────────

    /**
     * @param wineId null = manual input, non-null = from catalog prefilled
     */
    @Serializable data class AddTasting(val wineId: String? = null) : NavKey

    @Serializable data class WineDetail(val wineId: String) : NavKey

    // TODO: Onboarding / settings / about / etc. screens
}

val ROOT_KEYS = setOf(NavKey.Dashboard, NavKey.WineList, NavKey.Stats)