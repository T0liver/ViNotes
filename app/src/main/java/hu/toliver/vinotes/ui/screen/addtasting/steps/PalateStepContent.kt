package hu.toliver.vinotes.ui.screen.addtasting.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import hu.toliver.vinotes.ui.screen.UIConverter.toDisplayName
import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingEvent
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingState
import hu.toliver.vinotes.ui.screen.addtasting.components.SegmentedSelector
import hu.toliver.vinotes.ui.screen.addtasting.components.TastingSlider

@Composable
fun PalateStepContent(
    state: AddTastingState,
    onEvent: (AddTastingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            SegmentedSelector(
                entries = WineSweetness.entries,
                current = state.sweetness,
                label = stringResource(R.string.sweetness),
                labelOf = { it.toDisplayName(context) },
                onChange = { onEvent(AddTastingEvent.SweetnessChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.acidity,
                label = stringResource(R.string.acidity),
                startLabel = stringResource(R.string.very_low),
                endLabel = stringResource(R.string.very_high),
                onChange = { onEvent(AddTastingEvent.AcidityChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.tannin,
                label = stringResource(R.string.tannin),
                startLabel = stringResource(R.string.soft),
                endLabel = stringResource(R.string.bold),
                onChange = { onEvent(AddTastingEvent.TanninChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.body,
                label = stringResource(R.string.body),
                startLabel = stringResource(R.string.light),
                endLabel = stringResource(R.string.full),
                onChange = { onEvent(AddTastingEvent.BodyChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.alcohol,
                label = stringResource(R.string.alcohol),
                startLabel = stringResource(R.string.low),
                endLabel = stringResource(R.string.high),
                onChange = { onEvent(AddTastingEvent.AlcoholChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Intensity.entries,
                current = state.flavourIntensity,
                label = stringResource(R.string.flavour_intensity),
                startLabel = stringResource(R.string.weak),
                endLabel = stringResource(R.string.strong),
                onChange = { onEvent(AddTastingEvent.FlavourIntensityChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.finish,
                label = stringResource(R.string.finish),
                startLabel = stringResource(R.string.tshort),
                endLabel = stringResource(R.string.tlong),
                onChange = { onEvent(AddTastingEvent.FinishChanged(it)) },
            )
        }
        item {
            OutlinedTextField(
                value = state.flavourCharacteristics,
                onValueChange = { onEvent(AddTastingEvent.FlavourCharacteristicsChanged(it)) },
                label = { Text(stringResource(R.string.flavour_characteristics)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
            )
        }
        item {
            OutlinedTextField(
                value = state.otherPalate,
                onValueChange = { onEvent(AddTastingEvent.OtherPalateChanged(it)) },
                label = { Text(stringResource(R.string.other_palate_notes)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
            )
        }
    }
}
