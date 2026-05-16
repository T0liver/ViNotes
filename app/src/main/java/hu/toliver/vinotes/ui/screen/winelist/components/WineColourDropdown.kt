package hu.toliver.vinotes.ui.screen.winelist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import hu.toliver.vinotes.data.local.converters.EnumConverter.toDisplayName
import hu.toliver.vinotes.domain.model.enums.WineColour

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
