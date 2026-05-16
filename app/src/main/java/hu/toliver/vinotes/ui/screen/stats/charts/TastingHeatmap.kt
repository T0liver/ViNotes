package hu.toliver.vinotes.ui.screen.stats.charts

import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar.DAY_OF_WEEK
import java.util.Calendar.DAY_OF_YEAR
import java.util.Calendar.MONDAY
import java.util.Calendar.getInstance
import java.util.Locale

@Composable
fun TastingHeatmap(
    tastingsByDay: Map<String, Int>,
    selectedDay: String?,
    onDayTapped: (day: String, count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val primaryColor = colorScheme.primary
    val surfaceVariant = colorScheme.surfaceVariant
    val todayOutline = colorScheme.onSurface

    val weeks: List<List<String>> = remember {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = getInstance()
        while (cal.get(DAY_OF_WEEK) != MONDAY)
            cal.add(DAY_OF_YEAR, -1)
        cal.timeInMillis
        val days = mutableListOf<String>()
        val endCal = getInstance()
        while (cal.before(endCal) || cal == endCal) {
            days.add(sdf.format(cal.time))
            cal.add(DAY_OF_YEAR, 1)
        }
        days.takeLast(18 * 7).chunked(7)
    }

    val today = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .format(getInstance().time)
    }

    val dayLabels = listOf("M", "Tu", "W", "Th", "F", "Sa", "Su")

    Column(modifier = modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Spacer(Modifier.width(0.dp))
            dayLabels.forEach { day ->
                Text(
                    text = day,
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant,
                    modifier = Modifier.width(14.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            items(weeks) { week ->
                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    week.forEach { day ->
                        val count = tastingsByDay[day] ?: 0
                        val isToday = day == today
                        val isSelected = day == selectedDay
                        val cellColor = when (count) {
                            0 -> surfaceVariant
                            1 -> primaryColor.copy(alpha = 0.35f)
                            2 -> primaryColor.copy(alpha = 0.65f)
                            else -> primaryColor
                        }
                        val scale by animateFloatAsState(
                            targetValue = if (isSelected) 1.3f else 1f,
                            animationSpec = spring(dampingRatio = DampingRatioMediumBouncy)
                        )

                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .scale(scale)
                                .clip(RoundedCornerShape(3.dp))
                                .background(cellColor)
                                .then(
                                    if (isToday || isSelected) Modifier.border(
                                        width = 1.5.dp,
                                        color = if (isSelected) colorScheme.onSurface else todayOutline.copy(
                                            alpha = 0.6f
                                        ),
                                        shape = RoundedCornerShape(3.dp)
                                    )
                                    else Modifier
                                )
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }) {
                                    onDayTapped(
                                        day,
                                        count
                                    )
                                }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Less",
                style = typography.labelSmall,
                color = colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(4.dp))
            listOf(0f, 0.35f, 0.65f, 1f).forEach { alpha ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (alpha == 0f) surfaceVariant else primaryColor.copy(alpha = alpha))
                )
                Spacer(Modifier.width(3.dp))
            }
            Text(
                "More",
                style = typography.labelSmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun HeatmapTooltip(day: String, count: Int, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    val formatted = remember(day) {
        runCatching {
            val inSdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val outSdf = SimpleDateFormat("yyyy. MMM d.", Locale.GERMAN)
            outSdf.format(inSdf.parse(day)!!)
        }.getOrDefault(day)
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = shapes.small,
        color = colorScheme.secondaryContainer,
        onClick = onDismiss
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(formatted, style = typography.bodyMedium)
            Text(
                text = if (count == 0) "No tastings yet" else "$count tastings",
                style = typography.bodyMedium,
                color = if (count > 0) colorScheme.primary else colorScheme.onSurfaceVariant
            )
        }
    }
}

