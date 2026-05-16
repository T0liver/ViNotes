package hu.toliver.vinotes.ui.screen.winelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.WineWithStats


@Composable
fun WineList(
    wines: List<WineWithStats>,
    onCardClick: (String) -> Unit,
    onCardLongPress: (Wine) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(wines, key = { it.wine.id }) { item ->
            WineCard(
                item = item,
                onClick = { onCardClick(item.wine.id) },
                onLongPress = { onCardLongPress(item.wine) },
            )
        }
    }
}