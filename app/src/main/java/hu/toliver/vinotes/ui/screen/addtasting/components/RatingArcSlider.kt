package hu.toliver.vinotes.ui.screen.addtasting.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun RatingArcSlider(
    rating: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val trackColor = MaterialTheme.colorScheme.surfaceVariant

    val ratingColor by animateColorAsState(
        targetValue = when {
            rating >= 90 -> primaryColor
            rating >= 75 -> Color(0xFF9B1B30)
            rating >= 60 -> Color(0xFFD4A843)
            else -> Color(0xFFC4682A)
        },
        label = "rating_color",
    )

    val animatedRating by animateFloatAsState(
        targetValue = rating.toFloat(),
        animationSpec = spring(),
        label = "rating_value",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(2f),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f)
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val center = Offset(size.width / 2f, size.height.toFloat())
                        val angle = atan2(
                            (center.y - change.position.y).toDouble(),
                            (change.position.x - center.x).toDouble(),
                        )
                        val degrees = Math.toDegrees(angle).coerceIn(0.0, 180.0)
                        val newRating = 100 - (degrees / 180.0 * 100).roundToInt().coerceIn(0, 100)
                        onChange(newRating)
                    }
                }
        ) {
            val cx = size.width / 2f
            val cy = size.height
            val radius = size.width / 2f * 0.85f
            val strokeW = 14.dp.toPx()

            drawArc(
                color = trackColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                style = Stroke(width = strokeW),
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
            )

            drawArc(
                color = ratingColor,
                startAngle = 180f,
                sweepAngle = animatedRating / 100f * 180f,
                useCenter = false,
                style = Stroke(width = strokeW),
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
            )

            val thumbAngle = Math.toRadians(180 - animatedRating / 100.0 * 180.0)
            val thumbX = cx + radius * cos(thumbAngle).toFloat()
            val thumbY = cy - radius * sin(thumbAngle).toFloat()
            drawCircle(color = Color.White, radius = 10.dp.toPx(), center = Offset(thumbX, thumbY))
            drawCircle(color = ratingColor, radius = 7.dp.toPx(), center = Offset(thumbX, thumbY))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center),
        ) {
            Text(
                text = rating.toString(),
                style = MaterialTheme.typography.displaySmall,
                color = ratingColor,
            )
            Text(
                text = stringResource(ratingLabel(rating)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private fun ratingLabel(rating: Int): Int = when {
    rating >= 95 -> R.string.classic
    rating >= 90 -> R.string.superior
    rating >= 85 -> R.string.very_good
    rating >= 80 -> R.string.good
    rating >= 70 -> R.string.average
    rating >= 60 -> R.string.below_average
    rating >= 50 -> R.string.unacceptable
    else -> R.string.simple
}
