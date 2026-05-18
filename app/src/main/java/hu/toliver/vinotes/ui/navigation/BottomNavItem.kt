package hu.toliver.vinotes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import hu.toliver.vinotes.R

data class BottomNavItem(
    val key: Screen,
    @StringRes val labelRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        key = Screen.Dashboard,
        labelRes = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        key = Screen.WineList,
        labelRes = R.string.basement,
        selectedIcon = Icons.Filled.LocalBar,
        unselectedIcon = Icons.Outlined.LocalBar,
    ),
    BottomNavItem(
        key = Screen.Stats,
        labelRes = R.string.stats,
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
    ),
)