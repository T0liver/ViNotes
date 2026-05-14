package hu.toliver.vinotes.ui.screen.winelist

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.domain.model.enums.WineColour
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WineFilterSheet(
    currentFilters: WineFilters,
    availableColours: List<WineColour>,
    availableCountries: List<String>,
    onApply: (WineFilters) -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    var selectedColours by remember { mutableStateOf(currentFilters.colours) }
    var selectedCountries by remember { mutableStateOf(currentFilters.countries) }
    var yearFromText by remember { mutableStateOf(currentFilters.yearFrom?.toString() ?: "") }
    var yearToText by remember { mutableStateOf(currentFilters.yearTo?.toString() ?: "") }

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
            // ── Fejléc ──────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Szűrők",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }
                        onDismiss()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Bezárás")
                    }
                }
            }

            // ── Bor színe ───────────────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Bor színe",
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
                                label = { Text(getWineColourDisplayName(colour), style = MaterialTheme.typography.labelSmall) },
                            )
                        }
                    }
                }
            }

            // ── Ország ──────────────────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Ország",
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

            // ── Évjárat ────────────────────────────────────────────
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Évjárat",
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
                            label = { Text("Tól") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = yearToText,
                            onValueChange = { yearToText = it },
                            label = { Text("Ig") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                    }
                }
            }

            // ── Gombok ──────────────────────────────────────────────
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
                        Text("Szűrők törlése")
                    }
                    Button(
                        onClick = {
                            val filters = WineFilters(
                                colours = selectedColours,
                                countries = selectedCountries,
                                yearFrom = yearFromText.toIntOrNull(),
                                yearTo = yearToText.toIntOrNull(),
                            )
                            onApply(filters)
                            scope.launch { sheetState.hide() }
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Text("Alkalmazás")
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

fun getWineColourDisplayName(colour: WineColour): String = when (colour) {
    WineColour.GRAY -> "Szürke"
    WineColour.ORANGE -> "Narancs"
    WineColour.WHITE -> "Fehér"
    WineColour.YELLOW -> "Sárga"
    WineColour.ROSE -> "Rozé"
    WineColour.SHILLER -> "Rotgold"
    WineColour.TAWNY -> "Tawny"
    WineColour.RED -> "Vörös"
}



