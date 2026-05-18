package hu.toliver.vinotes.ui.screen.winelist.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import hu.toliver.vinotes.R
import hu.toliver.vinotes.data.local.converters.UIConverter.toDisplayName
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
            label = { Text(stringResource(R.string.colour_req)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
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
