package hu.toliver.vinotes.ui.screen.stats.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun RatingDistributionChart(
    buckets: Map<Int, Int>,
    modifier: Modifier = Modifier,
    playAnimations: Boolean = true,
    onPlayed: (() -> Unit)? = null,
) {
    val maxCount = buckets.values.maxOrNull()?.coerceAtLeast(1) ?: 1

    val bucketBounds = listOf(95, 90, 85, 80, 75, 70, 60, 50)
    val bucketLabels = mapOf(
        95 to "95–100", 90 to "90–94", 85 to "85–89",
        80 to "80–84", 75 to "75–79", 70 to "70–74",
        60 to "60–69", 50 to "50–59",
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        bucketBounds.forEach { lower ->
            val count = buckets[lower] ?: 0
            val label = bucketLabels[lower] ?: "$lower+"
            val ratio = count.toFloat() / maxCount

            var started by remember { mutableStateOf(false) }
            val animWidth by animateFloatAsState(
                targetValue = if (playAnimations) {
                    if (started) ratio else 0f
                } else {
                    ratio
                },
                animationSpec = tween(700)
            )
            LaunchedEffect(playAnimations) {
                if (playAnimations) {
                    started = true
                    onPlayed?.invoke()
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(52.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(20.dp)
                        .clip(shapes.extraSmall)
                        .background(colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animWidth)
                            .clip(shapes.extraSmall)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        colorScheme.primary,
                                        colorScheme.primary.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                }
                Text(
                    text = count.toString(),
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .width(28.dp)
                        .padding(start = 4.dp)
                )
            }
        }
    }
}