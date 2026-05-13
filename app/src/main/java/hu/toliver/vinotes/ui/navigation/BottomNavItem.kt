package hu.toliver.vinotes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val key:           NavKey,
    val label:         String,
    val selectedIcon:  ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        key           = NavKey.Dashboard,
        label         = "Home",
        selectedIcon  = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        key           = NavKey.WineList,
        label         = "Basement",
        selectedIcon  = Icons.Filled.LocalBar,
        unselectedIcon = Icons.Outlined.LocalBar,
    ),
    BottomNavItem(
        key           = NavKey.Stats,
        label         = "Stats",
        selectedIcon  = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
    ),
)