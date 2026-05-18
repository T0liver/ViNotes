package hu.toliver.vinotes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import hu.toliver.vinotes.R

data class BottomNavItem(
    val key: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        key = Screen.Dashboard,
        label = R.string.home.toString(),
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        key = Screen.WineList,
        label = R.string.basement.toString(),
        selectedIcon = Icons.Filled.LocalBar,
        unselectedIcon = Icons.Outlined.LocalBar,
    ),
    BottomNavItem(
        key = Screen.Stats,
        label = R.string.stats.toString(),
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
    ),
)