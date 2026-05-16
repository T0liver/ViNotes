package hu.toliver.vinotes.ui.screen.addtasting.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SegmentedSelector(
    entries: List<T>,
    current: T,
    labelOf: (T) -> String,
    onChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
) {
    Column(modifier = modifier) {
        if (label.isNotBlank()) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            entries.forEach { entry ->
                val selected = entry == current
                val bgColor by animateColorAsState(
                    if (selected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface,
                    label = "seg_bg_${entry}",
                )
                val textColor by animateColorAsState(
                    if (selected) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    label = "seg_txt_${entry}",
                )
                Surface(
                    onClick = { onChange(entry) },
                    shape = MaterialTheme.shapes.small,
                    color = bgColor,
                    border = BorderStroke(
                        width = if (selected) 0.dp else 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    Text(
                        text = labelOf(entry),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    )
                }
            }
        }
    }
}
