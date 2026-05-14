package hu.toliver.vinotes.ui.screen.winedetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import hu.toliver.vinotes.data.local.converters.EnumConverter.fromColourToHex
import hu.toliver.vinotes.data.local.converters.EnumConverter.toDisplayName
import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.ui.screen.dashboard.SectionHeader
import hu.toliver.vinotes.ui.theme.RatingGold
import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormatter = SimpleDateFormat("yyyy. MMM d.", Locale.forLanguageTag("hu"))

@Composable
fun WineDetailContent(
    state: WineDetailState,
    onEvent: (WineDetailEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val wine = state.wine!!

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            WineHeroSection(
                wine = wine,
                tastingCount = state.tastings.size,
                averageRating = state.tastings.map { it.rating }.average()
                    .takeIf { state.tastings.isNotEmpty() },
            )
        }

        item {
            WineDetailsCard(
                wine = wine,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        state.radarData?.let { radar ->
            item {
                WineRadarSection(
                    radarData = radar,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        item {
            SectionHeader(
                title = "Tastings",
                actionLabel = "+ tasting",
                onAction = { onEvent(WineDetailEvent.AddTastingClicked) },
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }

        if (state.tastings.isEmpty()) {
            item { WineNoTastingsPlaceholder(onAddClick = { onEvent(WineDetailEvent.AddTastingClicked) }) }
        } else {
            items(state.tastings, key = { it.id }) { taste ->
                TastingListItem(
                    taste = taste,
                    onClick = { onEvent(WineDetailEvent.TastingClicked(taste.id)) },
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
        }

        item {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onEvent(WineDetailEvent.DeleteWineClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Delete Wine")
            }
        }
    }
}

@Composable
fun WineHeroSection(
    wine: Wine,
    tastingCount: Int,
    averageRating: Double?,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(80.dp)
                    .background(
                        color = wine.colour.fromColourToHex().toComposeColor(),
                        shape = RoundedCornerShape(2.dp),
                    ),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = wine.name,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )

                Text(
                    text = "${wine.producer} · ${wine.year}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (tastingCount > 0) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        if (averageRating != null) {
                            RatingBadgeWineDetail(rating = averageRating.toInt())
                            Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = "$tastingCount tasting",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = wine.colour.toDisplayName(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                } else {
                    Text(
                        text = "No tasting yet",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun WineDetailsCard(
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

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TastingListItem(
    taste: Taste,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RatingBadgeWineDetail(rating = taste.rating)
                Spacer(Modifier.weight(1f))
                Text(
                    text = dateFormatter.format(taste.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text("·", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = taste.place.ifBlank { "–" },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (taste.overallImpression.isNotBlank()) {
                Text(
                    text = taste.overallImpression,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (taste.wouldDrinkAgain) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp),
                ) {
                    Icon(
                        Icons.Outlined.Replay,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Would drink again",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
fun WineNoTastingsPlaceholder(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = "No tastings added yet for this wine",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onAddClick) {
            Text("Add tasting")
        }
    }
}

@Composable
fun WineDetailLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun WineDetailErrorContent(
    message: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(24.dp))
        OutlinedButton(onClick = onBack) {
            Text("Vissza")
        }
    }
}

// Helper components
@Composable
private fun RatingBadgeWineDetail(rating: Int) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.primaryContainer,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = RatingGold,
                modifier = Modifier.size(12.dp),
            )
            Text(
                text = "★ $rating",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}


private fun String.toComposeColor(): Color = Color(this.toColorInt())