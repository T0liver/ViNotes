package hu.toliver.vinotes.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import hu.toliver.vinotes.R

data class BottomNavItem(
    val key: Screen,
    @param:StringRes val labelRes: Int,
    val selectedIcon: ImageVector? = null,
    val unselectedIcon: ImageVector? = null,
    val selectedPainter: Painter? = null,
    val unselectedPainter: Painter? = null,
)

@Composable
fun getBottomNavItems(): List<BottomNavItem> = listOf(
    BottomNavItem(
        key = Screen.Dashboard,
        labelRes = R.string.home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        key = Screen.WineList,
        labelRes = R.string.basement,
        selectedPainter = painterResource(id = R.drawable.cask_filled),
        unselectedPainter = painterResource(id = R.drawable.cask_outlined),
    ),
    BottomNavItem(
        key = Screen.Stats,
        labelRes = R.string.stats,
        selectedIcon = Icons.Filled.BarChart,
        unselectedIcon = Icons.Outlined.BarChart,
    ),
)