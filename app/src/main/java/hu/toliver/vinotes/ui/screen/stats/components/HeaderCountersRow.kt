package hu.toliver.vinotes.ui.screen.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.domain.model.FullStatsData

@Composable
fun HeaderCountersRow(
    data: FullStatsData,
    modifier: Modifier = Modifier,
    playAnimations: Boolean = true,
    onPlayed: (() -> Unit)? = null,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AnimatedStatCounter(
            targetValue = data.totalWines,
            label = "Number of wines",
            modifier = Modifier.weight(1f),
            playAnimations = playAnimations,
            onPlayed = onPlayed,
        )
        AnimatedStatCounter(
            targetValue = data.totalTastings,
            label = "Tastings",
            modifier = Modifier.weight(1f),
            playAnimations = playAnimations,
        )
        AnimatedStatCounter(
            targetValue = data.averageRating.toInt(),
            label = "Average",
            modifier = Modifier.weight(1f),
            playAnimations = playAnimations,
        )
        AnimatedStatCounter(
            targetValue = data.wouldDrinkAgainPct,
            label = "Would drink again (%)",
            modifier = Modifier.weight(1f),
            playAnimations = playAnimations,
        )
    }
}

