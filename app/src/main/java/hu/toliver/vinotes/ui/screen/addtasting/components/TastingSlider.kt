package hu.toliver.vinotes.ui.screen.addtasting.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import hu.toliver.vinotes.ui.screen.UIConverter.toDisplayName
import kotlin.math.roundToInt

/**
 * @param T        Type of the entries (usually an enum)
 * @param entries  The enum values in order (Level.entries etc.)
 * @param current  The currently selected value
 * @param label    The slider label (e.g., "Acidity")
 * @param startLabel The leftmost label (e.g., "Low")
 * @param endLabel   The rightmost label (e.g., "High")
 * @param onChange The callback for the selected value
 */
@Composable
fun <T> TastingSlider(
    entries: List<T>,
    current: T,
    label: String,
    startLabel: String,
    endLabel: String,
    onChange: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    val index = entries.indexOf(current).coerceAtLeast(0)
    val animatedIndex by animateFloatAsState(
        targetValue = index.toFloat(),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "slider_$label",
    )

    val currentText = when (current) {
        is WineColourIntensity -> current.toDisplayName()
        is Intensity -> current.toDisplayName()
        is WineSweetness -> current.toDisplayName()
        is Level -> current.toDisplayName()
        else -> current.toString().replace("_", " ").lowercase()
            .replaceFirstChar { it.uppercase() }
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium)
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                Text(
                    text = currentText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Slider(
            value = animatedIndex,
            onValueChange = { raw ->
                val i = raw.roundToInt().coerceIn(entries.indices)
                if (entries[i] != current) onChange(entries[i])
            },
            valueRange = 0f..(entries.size - 1).toFloat(),
            steps = entries.size - 2,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                startLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                endLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
