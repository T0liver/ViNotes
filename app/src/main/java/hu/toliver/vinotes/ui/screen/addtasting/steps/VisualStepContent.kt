package hu.toliver.vinotes.ui.screen.addtasting.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import hu.toliver.vinotes.data.local.converters.UIConverter.toDisplayName
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingEvent
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingState
import hu.toliver.vinotes.ui.screen.addtasting.components.SegmentedSelector
import hu.toliver.vinotes.ui.screen.addtasting.components.TastingSlider
import hu.toliver.vinotes.ui.screen.addtasting.components.WineColourPicker

@Composable
fun VisualStepContent(
    state: AddTastingState,
    onEvent: (AddTastingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            WineColourPicker(
                current = state.colour,
                onChange = { onEvent(AddTastingEvent.ColourChanged(it)) },
            )
        }
        item {
            SegmentedSelector(
                entries = WineClarity.entries,
                current = state.clarity,
                label = stringResource(R.string.clarity),
                labelOf = { it.toDisplayName() },
                onChange = { onEvent(AddTastingEvent.ClarityChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = WineColourIntensity.entries,
                current = state.colourIntensity,
                label = stringResource(R.string.colour_intensity),
                startLabel = stringResource(R.string.light),
                endLabel = stringResource(R.string.dark),
                onChange = { onEvent(AddTastingEvent.ColourIntensityChanged(it)) },
            )
        }
        item {
            OutlinedTextField(
                value = state.otherVisual,
                onValueChange = { onEvent(AddTastingEvent.OtherVisualChanged(it)) },
                label = { Text(stringResource(R.string.other_visual_notes)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
            )
        }
    }
}
