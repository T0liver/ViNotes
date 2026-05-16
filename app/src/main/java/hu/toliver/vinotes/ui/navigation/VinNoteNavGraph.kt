package hu.toliver.vinotes.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingScreen
import hu.toliver.vinotes.ui.screen.dashboard.DashboardScreen
import hu.toliver.vinotes.ui.screen.settings.SettingsScreen
import hu.toliver.vinotes.ui.screen.stats.StatsScreen
import hu.toliver.vinotes.ui.screen.winelist.WineListScreen
import hu.toliver.vinotes.ui.screen.winedetail.WineDetailScreen

@Composable
fun VinNoteNavGraph() {
    val backStack = rememberNavBackStack(Screen.Dashboard)
    val currentDest: Screen? by remember {
        derivedStateOf { backStack.lastOrNull() as? Screen }
    }

    Scaffold(
        bottomBar = {
            if (currentDest in ROOT_DESTS) {
                ViNotesBottomBar(
                    items       = bottomNavItems,
                    currentDest = currentDest,
                    onItemClick = { item ->
                        if (currentDest != item.key) {
                            backStack.clear()
                            backStack.add(item.key)
                        }
                    },
                )
            }
        },
    ) { innerPadding ->

        NavDisplay(
            backStack = backStack,
            modifier  = Modifier.padding(innerPadding),
            onBack    = {
                if (backStack.size > 1) backStack.removeLastOrNull()
            },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),

            entryProvider = entryProvider {

                entry<Screen.Dashboard> {
                    DashboardScreen(
                        onNavigateToSettings   = { backStack.add(Screen.Settings) },
                        onNavigateToWineList    = { backStack.add(Screen.WineList) },
                        onNavigateToDetail      = { wineId, _ -> backStack.add(Screen.WineDetail(wineId)) },
                        onNavigateToAddTasting  = { backStack.add(Screen.AddTasting()) },
                    )
                }

                entry<Screen.Settings> {
                    SettingsScreen(
                        onNavigateUp = { backStack.removeLastOrNull() },
                    )
                }

                entry<Screen.WineList> {
                    WineListScreen(
                        onNavigateToDetail = { wineId -> backStack.add(Screen.WineDetail(wineId)) },
                    )
                }

                entry<Screen.Stats> {
                    StatsScreen()
                }

                entry<Screen.WineDetail> { key ->
                    WineDetailScreen(
                        wineId = key.wineId,
                        onNavigateUp = { backStack.removeLastOrNull() },
                        onAddTasting = { wineId ->
                            backStack.add(Screen.AddTasting(wineId))
                        },
                    )
                }

                entry<Screen.AddTasting> { key ->
                    AddTastingScreen(
                        preselectedWineId = key.wineId,
                        onNavigateUp = { backStack.removeLastOrNull() },
                    )
                }
            },
        )
    }
}

@Composable
private fun ViNotesBottomBar(
    items:       List<BottomNavItem>,
    currentDest: Screen?,
    onItemClick: (BottomNavItem) -> Unit,
) {
    NavigationBar {
        items.forEach { item ->
            val selected = currentDest == item.key

            NavigationBarItem(
                selected = selected,
                onClick  = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
            )
        }
    }
}