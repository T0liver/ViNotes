package hu.toliver.vinotes.ui.screen.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PeriodStatsRow(
    tastings: Int,
    avgRating: Double,
    modifier: Modifier = Modifier,
    playAnimations: Boolean = true,
    onPlayed: (() -> Unit)? = null,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.fillMaxWidth()) {
        AnimatedStatCounter(
            targetValue = tastings,
            label = "Tastings in period",
            modifier = Modifier.weight(1f),
            playAnimations = playAnimations,
            onPlayed = onPlayed,
        )
        AnimatedStatCounter(
            targetValue = avgRating.toInt(),
            label = "Average score",
            modifier = Modifier.weight(1f),
            playAnimations = playAnimations,
        )
    }
}