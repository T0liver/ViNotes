package hu.toliver.vinotes.ui.screen.winelist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WineFormSheet(
    editingWine: Wine?,
    onSave: (Wine, Boolean) -> Unit,
    onDelete: (Wine) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val isEdit = editingWine != null
    val isNew = !isEdit

    var name by remember { mutableStateOf(editingWine?.name ?: "") }
    var producer by remember { mutableStateOf(editingWine?.producer ?: "") }
    var year by remember { mutableStateOf(editingWine?.year?.toString() ?: "") }
    var grape by remember { mutableStateOf(editingWine?.grape ?: "") }
    var isCuvee by remember { mutableStateOf(editingWine?.isCuvee ?: false) }
    var cuveeText by remember { mutableStateOf(editingWine?.cuveeComponents?.joinToString(", ") ?: "") }
    var selectedColour by remember { mutableStateOf(editingWine?.colour ?: WineColour.RED) }
    var country by remember { mutableStateOf(editingWine?.country ?: "") }
    var region by remember { mutableStateOf(editingWine?.region ?: "") }
    var alcoholText by remember { mutableStateOf(editingWine?.alcoholPercentage?.toString() ?: "") }
    var description by remember { mutableStateOf(editingWine?.description ?: "") }

    var showDeleteDialog by remember { mutableStateOf(false) }

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
                        text = if (isNew) stringResource(R.string.add_new_wine) else stringResource(
                            R.string.edit_wine
                        ),
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
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.wine_name_req)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = producer,
                    onValueChange = { producer = it },
                    label = { Text(stringResource(R.string.producer_req)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text(stringResource(R.string.year_req)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = grape,
                    onValueChange = { grape = it },
                    label = { Text(stringResource(R.string.vine_type_req)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(stringResource(R.string.cuvee_q), style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = isCuvee, onCheckedChange = { isCuvee = it })
                }
            }

            if (isCuvee) {
                item {
                    OutlinedTextField(
                        value = cuveeText,
                        onValueChange = { cuveeText = it },
                        label = { Text(stringResource(R.string.cuvee_ingredient)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("pl. Merlot, Cabernet Sauvignon") },
                    )
                }
            }

            item {
                WineColourDropdown(
                    selected = selectedColour,
                    onSelect = { selectedColour = it },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text(stringResource(R.string.country)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text(stringResource(R.string.region)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = alcoholText,
                    onValueChange = { alcoholText = it },
                    label = { Text(stringResource(R.string.alcohol_perc)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4,
                    minLines = 3,
                )
            }

            if (isEdit) {
                item {
                    OutlinedButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                    ) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.delete_wine))
                    }
                }
            }

            item {
                val isValid = name.isNotBlank() && producer.isNotBlank()
                    && year.toIntOrNull() != null && grape.isNotBlank()

                Button(
                    onClick = {
                        val wine = Wine(
                            id = editingWine?.id ?: UUID.randomUUID().toString(),
                            name = name,
                            producer = producer,
                            year = year.toInt(),
                            grape = grape,
                            isCuvee = isCuvee,
                            cuveeComponents = if (isCuvee) cuveeText.split(",").map { it.trim() } else emptyList(),
                            colour = selectedColour,
                            sugar = 0f,
                            sweetness = WineSweetness.DRY,
                            country = country,
                            region = region,
                            alcoholPercentage = alcoholText.toDoubleOrNull() ?: 0.0,
                            volume = 750,
                            description = description,
                            image = editingWine?.image ?: "",
                        )
                        onSave(wine, isNew)
                        scope.launch { sheetState.hide() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    enabled = isValid,
                ) {
                    Text(stringResource(R.string.save))
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showDeleteDialog && isEdit) {
        DeleteConfirmDialog(
            wineName = editingWine.name,
            onConfirm = {
                onDelete(editingWine)
                showDeleteDialog = false
                scope.launch { sheetState.hide() }
                onDismiss()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }
}