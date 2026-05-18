package hu.toliver.vinotes.ui.screen.tastingdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R

@Composable
fun TastingRatingSection(rating: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.rating),
                style = typography.titleMedium,
                color = colorScheme.onPrimaryContainer,
            )
            Text(
                text = "$rating/100",
                style = typography.headlineSmall,
                color = colorScheme.onPrimaryContainer,
            )
        }
    }
}