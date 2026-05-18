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
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingEvent
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingState
import hu.toliver.vinotes.ui.screen.addtasting.components.SegmentedSelector
import hu.toliver.vinotes.ui.screen.addtasting.components.TastingSlider

@Composable
fun NoseStepContent(
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
            TastingSlider(
                entries = Intensity.entries,
                current = state.noseIntensity,
                label = stringResource(R.string.nose_intensity),
                startLabel = stringResource(R.string.weak),
                endLabel = stringResource(R.string.strong),
                onChange = { onEvent(AddTastingEvent.NoseIntensityChanged(it)) },
            )
        }
        item {
            SegmentedSelector(
                entries = NoseDevelopment.entries,
                current = state.noseDevelopment,
                label = stringResource(R.string.nose_development),
                labelOf = { it.toDisplayName(context) },
                onChange = { onEvent(AddTastingEvent.NoseDevelopmentChanged(it)) },
            )
        }
        item {
            OutlinedTextField(
                value = state.noseAroma,
                onValueChange = { onEvent(AddTastingEvent.NoseAromaChanged(it)) },
                label = { Text(stringResource(R.string.aroma_characteristics)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
            )
        }
        item {
            OutlinedTextField(
                value = state.otherNose,
                onValueChange = { onEvent(AddTastingEvent.OtherNoseChanged(it)) },
                label = { Text(stringResource(R.string.other_nose_notes)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4,
            )
        }
    }
}
