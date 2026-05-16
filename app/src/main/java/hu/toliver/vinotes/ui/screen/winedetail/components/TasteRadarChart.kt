package hu.toliver.vinotes.ui.screen.winedetail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.ui.screen.winedetail.RadarData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TasteRadarChart(
    data: RadarData,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.outlineVariant
    val labelColor = MaterialTheme.colorScheme.onSurface
    val labelStyle = MaterialTheme.typography.labelSmall

    val labels = listOf("Acidity", "Tannin", "Body", "Alcohol", "Finish")

    val values = with(data) { listOf(acidity, tannin, body, alcohol, finish) }

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(40.dp)
    ) {
        val n = 5
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.width / 2f
        val levels = 4

        fun axisPoint(axisIndex: Int, value: Float): Offset {
            val angle = (2 * PI / n * axisIndex - PI / 2).toFloat()
            return Offset(
                x = center.x + radius * value * cos(angle),
                y = center.y + radius * value * sin(angle),
            )
        }

        for (level in 1..levels) {
            val r = level / levels.toFloat()
            val path = Path().apply {
                for (i in 0 until n) {
                    val p = axisPoint(i, r)
                    if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
                }
                close()
            }
            drawPath(path, color = gridColor, style = Stroke(width = 1.dp.toPx()))
        }

        for (i in 0 until n) {
            val p = axisPoint(i, 1f)
            drawLine(color = gridColor, start = center, end = p, strokeWidth = 1.dp.toPx())
        }

        val dataPath = Path().apply {
            for (i in 0 until n) {
                val p = axisPoint(i, values[i])
                if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
            }
            close()
        }
        drawPath(dataPath, color = primaryColor.copy(alpha = 0.25f))
        drawPath(dataPath, color = primaryColor, style = Stroke(width = 2.dp.toPx()))

        for (i in 0 until n) {
            val labelPos = axisPoint(i, 1.25f)
            val measured = textMeasurer.measure(
                text = AnnotatedString(labels[i]),
                style = labelStyle.copy(color = labelColor),
            )
            drawText(
                textLayoutResult = measured,
                topLeft = Offset(
                    x = labelPos.x - measured.size.width / 2f,
                    y = labelPos.y - measured.size.height / 2f,
                ),
            )
        }
    }
}
