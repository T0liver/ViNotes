package hu.toliver.vinotes.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable data object Dashboard : Screen
    @Serializable data object WineList : Screen
    @Serializable data object WineSelection : Screen
    @Serializable data object Stats : Screen
    @Serializable data object Settings : Screen

    @Serializable data class AddTasting(val wineId: String? = null) : Screen
    @Serializable data class WineDetail(val wineId: String) : Screen
    @Serializable data class TastingDetail(val tasteId: String) : Screen
}

val ROOT_DESTS = setOf(Screen.Dashboard, Screen.WineList, Screen.Stats)