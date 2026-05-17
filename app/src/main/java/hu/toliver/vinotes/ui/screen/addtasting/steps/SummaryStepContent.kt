package hu.toliver.vinotes.ui.screen.addtasting.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingEvent
import hu.toliver.vinotes.ui.screen.addtasting.AddTastingState
import hu.toliver.vinotes.ui.screen.addtasting.components.RatingArcSlider
import hu.toliver.vinotes.ui.screen.addtasting.components.WouldDrinkAgainToggle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SummaryStepContent(
    state: AddTastingState,
    onEvent: (AddTastingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = state.date.time,
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onEvent(AddTastingEvent.DateChanged(Date(it)))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            },
        ) { DatePicker(state = datePickerState) }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            RatingArcSlider(
                rating = state.rating,
                onChange = { onEvent(AddTastingEvent.RatingChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            )
        }
        item {
            OutlinedTextField(
                value = state.overallImpression,
                onValueChange = { onEvent(AddTastingEvent.OverallImpressionChanged(it)) },
                label = { Text("Overall impression") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6,
            )
        }
        item {
            OutlinedTextField(
                value = state.bestWith,
                onValueChange = { onEvent(AddTastingEvent.BestWithChanged(it)) },
                label = { Text("Best with") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.place,
                    onValueChange = { onEvent(AddTastingEvent.PlaceChanged(it)) },
                    label = { Text("Place") },
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(12.dp))
                Box(
                    modifier = Modifier.size(56.dp),
                ) {
                    if (state.isLoadingLocation) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(androidx.compose.ui.Alignment.Center),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        IconButton(
                            onClick = { onEvent(AddTastingEvent.OnFetchLocationClicked) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                Icons.Outlined.MyLocation,
                                contentDescription = "Fetch current location",
                            )
                        }
                    }
                }
            }
        }
        item {
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
            ) {
                Icon(
                    Icons.Outlined.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    SimpleDateFormat("yyyy. MM. dd.", Locale.forLanguageTag("hu")).format(state.date),
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                )
            }
        }
        item {
            WouldDrinkAgainToggle(
                value = state.wouldDrinkAgain,
                onChange = { onEvent(AddTastingEvent.WouldDrinkAgainChanged(it)) },
            )
        }
    }
}
