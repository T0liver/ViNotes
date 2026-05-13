package hu.toliver.vinotes.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen : NavKey {

    @Serializable data object Dashboard : Screen
    @Serializable data object WineList : Screen
    @Serializable data object Stats : Screen

    @Serializable data class AddTasting(val wineId: String? = null) : Screen
    @Serializable data class WineDetail(val wineId: String) : Screen
}

val ROOT_DESTS = setOf<Screen>(Screen.Dashboard, Screen.WineList, Screen.Stats)