package hu.toliver.vinotes.ui.screen.stats.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import hu.toliver.vinotes.ui.screen.UIConverter.fromColourToHex
import hu.toliver.vinotes.ui.screen.UIConverter.toDisplayName
import hu.toliver.vinotes.domain.model.enums.WineColour

@Composable
fun ColourDonutChart(
    distribution: Map<WineColour, Int>,
    modifier: Modifier = Modifier,
    playAnimations: Boolean = true,
    onPlayed: (() -> Unit)? = null,
) {
    val total   = distribution.values.sum().coerceAtLeast(1)
    val entries = distribution.entries.sortedByDescending { it.value }

    data class Arc(
        val colour: WineColour,
        val startAngle: Float,
        val sweepAngle: Float,
        val count: Int
    )

    val arcs = buildList {
        var current = -90f
        entries.forEach { (colour, count) ->
            val sweep = count.toFloat() / total * 360f
            add(Arc(colour, current, sweep, count))
            current += sweep
        }
    }

    var started by remember { mutableStateOf(false) }
    val targetProgress = if (playAnimations) {
        if (started) 1f else 0f
    } else {
        1f
    }
    val animProgress by animateFloatAsState(targetValue = targetProgress, animationSpec = tween(1000))
    LaunchedEffect(playAnimations) {
        if (playAnimations) {
            started = true
            onPlayed?.invoke()
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Canvas(modifier = Modifier
            .size(140.dp)
            .aspectRatio(1f)
        ) {
            val strokeWidth = 28.dp.toPx()
            val radius      = (size.minDimension / 2f) - strokeWidth / 2f
            val center      = Offset(size.width / 2f, size.height / 2f)

            arcs.forEach { arc ->
                val hex = arc.colour.fromColourToHex()
                val color = Color(hex.toColorInt())
                drawArc(
                    color = color,
                    startAngle = arc.startAngle,
                    sweepAngle = arc.sweepAngle * animProgress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            entries.take(5).forEach { (colour, count) ->
                val hex   = colour.fromColourToHex()
                val color = Color(hex.toColorInt())
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier
                        .size(10.dp)
                        .background(color, CircleShape)
                    )
                    Text(
                        text = "${colour.toDisplayName()}  $count",
                        style = typography.bodyMedium
                    )
                }
            }
        }
    }
}

