package hu.toliver.vinotes.ui.screen.addtasting.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.data.local.converters.UIConverter.toDisplayName
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
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            SegmentedSelector(
                entries = WineSweetness.entries,
                current = state.sweetness,
                label = "Sweetness",
                labelOf = { it.toDisplayName() },
                onChange = { onEvent(AddTastingEvent.SweetnessChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.acidity,
                label = "Acidity",
                startLabel = "Very Low",
                endLabel = "Very High",
                onChange = { onEvent(AddTastingEvent.AcidityChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.tannin,
                label = "Tannin",
                startLabel = "Soft",
                endLabel = "Bold",
                onChange = { onEvent(AddTastingEvent.TanninChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.body,
                label = "Body",
                startLabel = "Light",
                endLabel = "Full",
                onChange = { onEvent(AddTastingEvent.BodyChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.alcohol,
                label = "Alcohol",
                startLabel = "Low",
                endLabel = "High",
                onChange = { onEvent(AddTastingEvent.AlcoholChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Intensity.entries,
                current = state.flavourIntensity,
                label = "Flavour intensity",
                startLabel = "Weak",
                endLabel = "Strong",
                onChange = { onEvent(AddTastingEvent.FlavourIntensityChanged(it)) },
            )
        }
        item {
            TastingSlider(
                entries = Level.entries,
                current = state.finish,
                label = "Finish",
                startLabel = "Short",
                endLabel = "Long",
                onChange = { onEvent(AddTastingEvent.FinishChanged(it)) },
            )
        }
        item {
            OutlinedTextField(
                value = state.flavourCharacteristics,
                onValueChange = { onEvent(AddTastingEvent.FlavourCharacteristicsChanged(it)) },
                label = { Text("Flavour characteristics") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
            )
        }
        item {
            OutlinedTextField(
                value = state.otherPalate,
                onValueChange = { onEvent(AddTastingEvent.OtherPalateChanged(it)) },
                label = { Text("Other palate notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
            )
        }
    }
}
