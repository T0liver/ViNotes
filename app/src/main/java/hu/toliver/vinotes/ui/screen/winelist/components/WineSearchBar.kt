package hu.toliver.vinotes.ui.screen.winelist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.toliver.vinotes.ui.screen.winelist.WineSortOrder

@Composable
fun WineSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    sortOrder: WineSortOrder,
    onSortChange: (WineSortOrder) -> Unit,
    filterActive: Boolean,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = {
                Text("Search...", fontSize = 16.sp, lineHeight = 16.sp)
            },
            leadingIcon = { Icon(Icons.Outlined.WineBar, contentDescription = null, modifier = Modifier.size(20.dp)) },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
        )

        Box {
            IconButton(
                onClick = { sortMenuExpanded = true },
                modifier = Modifier.size(40.dp),
            ) {
                Icon(Icons.Outlined.SwapVert, contentDescription = "Sort")
            }

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false },
            ) {
                WineSortOrder.entries.forEach { order ->
                    DropdownMenuItem(
                        text = { Text(order.displayLabel()) },
                        onClick = {
                            onSortChange(order)
                            sortMenuExpanded = false
                        },
                    )
                }
            }
        }

        IconButton(
            onClick = onFilterClick,
            modifier = Modifier.size(40.dp),
        ) {
            if (filterActive) {
                BadgedBox(badge = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.error, RoundedCornerShape(4.dp))
                    )
                }) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Filters")
                }
            } else {
                Icon(Icons.Outlined.FilterList, contentDescription = "Filters")
            }
        }
    }
}

private fun WineSortOrder.displayLabel(): String = when (this) {
    WineSortOrder.NAME_ASC -> "Name (A -> Z)"
    WineSortOrder.RATING_DESC -> "Best rating"
    WineSortOrder.YEAR_DESC -> "Newest vintage"
    WineSortOrder.TASTING_DATE_DESC -> "Most recently tasted"
}
