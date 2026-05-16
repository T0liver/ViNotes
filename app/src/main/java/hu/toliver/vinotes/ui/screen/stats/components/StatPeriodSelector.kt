package hu.toliver.vinotes.ui.screen.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.domain.usecases.stats.StatPeriod

@Composable
fun StatPeriodSelector(
    selected: StatPeriod,
    onSelect: (StatPeriod) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        StatPeriod.entries.forEach { period ->
            FilterChip(
                selected = period == selected,
                onClick = { onSelect(period) },
                label = {
                    Text(
                        period.label,
                        style = typography.labelSmall,
                        textAlign = TextAlign.Center,
                        fontWeight = if (period == selected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}