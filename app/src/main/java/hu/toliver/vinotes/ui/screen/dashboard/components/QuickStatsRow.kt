package hu.toliver.vinotes.ui.screen.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.ui.theme.RatingGold


@Composable
fun QuickStatsRow(
    totalWines: Int,
    totalTastings: Int,
    averageRating: Double,
    topRegion: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        StatCard(
            icon = Icons.Outlined.WineBar,
            value = totalWines.toString(),
            label = "Wines",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            icon = Icons.AutoMirrored.Outlined.Assignment,
            value = totalTastings.toString(),
            label = "Taste",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            icon = Icons.Filled.Star,
            iconTint = RatingGold,
            value = "%.1f".format(averageRating),
            label = "Average",
            modifier = Modifier.weight(1f),
        )
        StatCard(
            icon = Icons.Outlined.LocationOn,
            value = topRegion.ifEmpty { "–" },
            label = "Top Region",
            modifier = Modifier.weight(1f),
        )
    }
}