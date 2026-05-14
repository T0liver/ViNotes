package hu.toliver.vinotes.ui.screen.winelist

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import hu.toliver.vinotes.data.local.converters.EnumConverter.toDisplayName
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import java.util.UUID
import kotlinx.coroutines.launch

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
                        text = if (isNew) "Add new wine" else "Edit wine",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    IconButton(onClick = {
                        scope.launch { sheetState.hide() }
                        onDismiss()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Wine name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = producer,
                    onValueChange = { producer = it },
                    label = { Text("Producer *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it },
                    label = { Text("Year *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = grape,
                    onValueChange = { grape = it },
                    label = { Text("Vine type *") },
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
                    Text("Cuvée?", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = isCuvee, onCheckedChange = { isCuvee = it })
                }
            }

            if (isCuvee) {
                item {
                    OutlinedTextField(
                        value = cuveeText,
                        onValueChange = { cuveeText = it },
                        label = { Text("Cuvée ingredient") },
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
                    label = { Text("Country") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = region,
                    onValueChange = { region = it },
                    label = { Text("Region") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = alcoholText,
                    onValueChange = { alcoholText = it },
                    label = { Text("Alcohol (%)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
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
                        Text("Delete wine")
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
                            sugar = 0,
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
                    Text("Save")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WineColourDropdown(
    selected: WineColour,
    onSelect: (WineColour) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selected.toDisplayName(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Colour *") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            WineColour.entries.forEach { colour ->
                DropdownMenuItem(
                    text = { Text(colour.toDisplayName()) },
                    onClick = {
                        onSelect(colour)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmDialog(
    wineName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = {
            Text("Delete wine")
        },
        text = {
            Text("Are you sure to delete \"$wineName\" wine? This operation cannot be undone.")
        },
    )
}