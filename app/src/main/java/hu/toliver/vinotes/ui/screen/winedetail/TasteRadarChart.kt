package hu.toliver.vinotes.ui.screen.winedetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
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

    val textMeasurer = androidx.compose.ui.text.rememberTextMeasurer()

    androidx.compose.foundation.Canvas(
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

@Composable
fun WineRadarSection(
    radarData: RadarData,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Taste Profile (average)",
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(8.dp))
            TasteRadarChart(
                data = radarData,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

