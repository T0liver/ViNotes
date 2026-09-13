package hu.toliver.vinotes.ui.screen.winelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import hu.toliver.vinotes.ui.screen.UIConverter.toDisplayName
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.ui.screen.winelist.WineFilters
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WineFilterSheet(
    sessionId: Int,
    currentFilters: WineFilters,
    availableColours: List<WineColour>,
    availableCountries: List<String>,
    onApply: (WineFilters) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()
    val filterKey = sessionId

    var selectedColours by rememberSaveable(filterKey) {
        mutableStateOf(currentFilters.colours.toList())
    }
    var selectedCountries by rememberSaveable(filterKey) {
        mutableStateOf(currentFilters.countries.toList())
    }
    var yearFromText by rememberSaveable(filterKey) { mutableStateOf(currentFilters.yearFrom?.toString() ?: "") }
    var yearToText by rememberSaveable(filterKey) { mutableStateOf(currentFilters.yearTo?.toString() ?: "") }

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onDismiss()
        },
        sheetState = sheetState,
        modifier = modifier,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.filters),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }
                        onDismiss()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.close))
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.colour),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        availableColours.forEach { colour ->
                            FilterChip(
                                selected = colour in selectedColours,
                                onClick = {
                                    selectedColours = if (colour in selectedColours) {
                                            selectedColours - colour
                                    } else {
                                            selectedColours + colour
                                    }
                                },
                                label = { Text(colour.toDisplayName(), style = MaterialTheme.typography.labelSmall) },
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.region),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        availableCountries.take(5).forEach { country ->
                            FilterChip(
                                selected = country in selectedCountries,
                                onClick = {
                                    selectedCountries = if (country in selectedCountries) {
                                            selectedCountries - country
                                    } else {
                                            selectedCountries + country
                                    }
                                },
                                label = { Text(country, style = MaterialTheme.typography.labelSmall) },
                            )
                        }
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.year),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            value = yearFromText,
                            onValueChange = { yearFromText = it },
                            label = { Text(stringResource(R.string.from)) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = yearToText,
                            onValueChange = { yearToText = it },
                            label = { Text(stringResource(R.string.til)) },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextButton(
                        onClick = onClear,
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.delete_filters))
                    }
                    Button(
                        onClick = {
                            val filters = WineFilters(
                                colours = selectedColours.toSet(),
                                countries = selectedCountries.toSet(),
                                yearFrom = yearFromText.toIntOrNull(),
                                yearTo = yearToText.toIntOrNull(),
                            )
                            onApply(filters)
                            scope.launch { sheetState.hide() }
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}