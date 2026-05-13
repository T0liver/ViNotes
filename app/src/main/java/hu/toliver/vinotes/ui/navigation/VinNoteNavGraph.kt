package hu.toliver.vinotes.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
private fun VinNoteBottomBar(
    items:      List<BottomNavItem>,
    currentKey: NavKey?,
    onItemClick: (BottomNavItem) -> Unit,
) {
    NavigationBar {
        items.forEach { item ->
            val selected = currentKey == item.key   // ← közvetlen key összehasonlítás

            NavigationBarItem(
                selected = selected,
                onClick  = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector        = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = { Text(item.label) },
            )
        }
    }
}