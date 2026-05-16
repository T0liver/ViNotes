package hu.toliver.vinotes.ui.screen.winedetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.data.local.converters.EnumConverter.toDisplayName
import hu.toliver.vinotes.domain.model.Wine

@Composable
fun WineDetailsCard(
    wine: Wine,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DetailRow(
                    label = "Vine type",
                    value = if (wine.isCuvee) "Cuvée: ${wine.cuveeComponents.joinToString(", ")}" else wine.grape,
                    modifier = Modifier.weight(1f),
                )
                DetailRow(
                    label = "Region",
                    value = wine.region.ifBlank { "–" },
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DetailRow(
                    label = "Country",
                    value = wine.country.ifBlank { "–" },
                    modifier = Modifier.weight(1f),
                )
                DetailRow(
                    label = "Alcohol",
                    value = "${wine.alcoholPercentage}%",
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                DetailRow(
                    label = "Sweetness",
                    value = wine.sweetness.toDisplayName(),
                    modifier = Modifier.weight(1f),
                )
                DetailRow(
                    label = "Colour",
                    value = wine.colour.toDisplayName(),
                    modifier = Modifier.weight(1f),
                )
            }

            if (wine.description.isNotBlank()) {
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = wine.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}