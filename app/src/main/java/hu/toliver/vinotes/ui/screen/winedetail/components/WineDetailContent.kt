package hu.toliver.vinotes.ui.screen.winedetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import hu.toliver.vinotes.ui.screen.dashboard.components.SectionHeader
import hu.toliver.vinotes.ui.screen.winedetail.WineDetailEvent
import hu.toliver.vinotes.ui.screen.winedetail.WineDetailState

@Composable
fun WineDetailContent(
    state: WineDetailState,
    onEvent: (WineDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wine = state.wine ?: return

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            WineHeroSection(
                wine = wine,
                tastingCount = state.tastings.size,
                averageRating = state.tastings.map { it.rating }.average()
                    .takeIf { state.tastings.isNotEmpty() },
            )
        }

        item {
            WineDetailsCard(
                wine = wine,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        state.radarData?.let { radar ->
            item {
                WineRadarSection(
                    radarData = radar,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        item {
            SectionHeader(
                title = stringResource(R.string.tastings),
                actionLabel = stringResource(R.string.add_tasting),
                onAction = { onEvent(WineDetailEvent.AddTastingClicked) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        if (state.tastings.isEmpty()) {
            item { WineNoTastingsPlaceholder(onAddClick = { onEvent(WineDetailEvent.AddTastingClicked) }) }
        } else {
            items(state.tastings, key = { it.id }) { taste ->
                TastingListItem(
                    taste = taste,
                    onClick = { onEvent(WineDetailEvent.TastingClicked(taste.id)) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onEvent(WineDetailEvent.DeleteWineClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.delete_wine))
            }
        }
    }
}