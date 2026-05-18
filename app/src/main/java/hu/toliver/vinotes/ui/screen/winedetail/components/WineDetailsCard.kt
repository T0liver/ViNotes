package hu.toliver.vinotes.ui.screen.winedetail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.ui.screen.UIConverter.toDisplayName

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
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            ) {
                DetailRow(
                    label = stringResource(R.string.vine_type),
                    value = if (wine.isCuvee) stringResource(
                        R.string.cuvee_list,
                        wine.cuveeComponents.joinToString(", ")
                    ) else wine.grape,
                    modifier = Modifier.weight(1f),
                )
                DetailRow(
                    label = stringResource(R.string.region),
                    value = wine.region.ifBlank { "–" },
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            ) {
                DetailRow(
                    label = stringResource(R.string.country),
                    value = wine.country.ifBlank { "–" },
                    modifier = Modifier.weight(1f),
                )
                DetailRow(
                    label = stringResource(R.string.alcohol),
                    value = "${wine.alcoholPercentage}%",
                    modifier = Modifier.weight(1f),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            ) {
                DetailRow(
                    label = stringResource(R.string.sweetness),
                    value = wine.sweetness.toDisplayName(),
                    modifier = Modifier.weight(1f),
                )
                DetailRow(
                    label = stringResource(R.string.colour),
                    value = wine.colour.toDisplayName(),
                    modifier = Modifier.weight(1f),
                )
            }

            if (wine.description.isNotBlank()) {
                Text(
                    text = stringResource(R.string.description),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = wine.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                SubcomposeAsyncImage(
                    model = wine.image,
                    contentDescription = wine.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Fit,
                ) {
                    when (painter.state) {
                        is coil.compose.AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator()
                        }
                        is coil.compose.AsyncImagePainter.State.Error -> {
                            Text(text = stringResource(R.string.image_load_failed) + "\n(${wine.image})")
                        }
                        else -> {
                            SubcomposeAsyncImageContent()
                        }
                    }
                }
            }
        }
    }
}