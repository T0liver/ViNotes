package hu.toliver.vinotes.ui.screen.stats.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R

@Composable
fun TopRegionsChart(
    regions: List<Pair<String, Int>>,
    modifier: Modifier = Modifier,
    playAnimations: Boolean = true,
    onPlayed: (() -> Unit)? = null,
) {
    val maxCount = regions.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 1

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        regions.forEachIndexed { idx, (region, count) ->
            val ratio = count.toFloat() / maxCount
            var started by remember { mutableStateOf(false) }
            val animWidth by animateFloatAsState(
                targetValue = if (playAnimations) {
                    if (started) ratio else 0f
                } else {
                    ratio
                },
                animationSpec = tween(600 + idx * 80)
            )
            LaunchedEffect(playAnimations) {
                if (playAnimations) {
                    started = true
                    onPlayed?.invoke()
                }
            }

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(region, style = typography.bodyMedium)
                    Text(
                        stringResource(R.string.amount_wine, count),
                        style = typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(shapes.extraSmall)
                        .background(colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animWidth)
                            .clip(shapes.extraSmall)
                            .background(colorScheme.secondary)
                    )
                }
            }
        }
    }
}