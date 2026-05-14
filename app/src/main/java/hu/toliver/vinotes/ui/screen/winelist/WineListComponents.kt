@file:Suppress("unused")

package hu.toliver.vinotes.ui.screen.winelist

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.WineWithStats
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.ui.theme.RatingGold

// ──────────────────────────────────────────────────────────────────────────────
// WineSearchBar – keresés + rendezés + szűrés
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun WineSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    sortOrder: WineSortOrder,
    onSortChange: (WineSortOrder) -> Unit,
    filterActive: Boolean,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text("Keresés...") },
            leadingIcon = { Icon(Icons.Outlined.WineBar, contentDescription = null, modifier = Modifier.size(20.dp)) },
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
        )

        // Sort button
        Box {
            IconButton(
                onClick = { sortMenuExpanded = true },
                modifier = Modifier.size(40.dp),
            ) {
                Icon(Icons.Outlined.SwapVert, contentDescription = "Rendezés")
            }

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false },
            ) {
                WineSortOrder.entries.forEach { order ->
                    DropdownMenuItem(
                        text = { Text(order.displayLabel()) },
                        onClick = {
                            onSortChange(order)
                            sortMenuExpanded = false
                        },
                    )
                }
            }
        }

        // Filter button
        IconButton(
            onClick = onFilterClick,
            modifier = Modifier.size(40.dp),
        ) {
            if (filterActive) {
                BadgedBox(badge = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(MaterialTheme.colorScheme.error, RoundedCornerShape(4.dp))
                    )
                }) {
                    Icon(Icons.Outlined.FilterList, contentDescription = "Szűrők")
                }
            } else {
                Icon(Icons.Outlined.FilterList, contentDescription = "Szűrők")
            }
        }
    }
}

private fun WineSortOrder.displayLabel(): String = when (this) {
    WineSortOrder.NAME_ASC -> "Név (A → Z)"
    WineSortOrder.RATING_DESC -> "Legjobb értékelés"
    WineSortOrder.YEAR_DESC -> "Legújabb évjárat"
    WineSortOrder.TASTING_DATE_DESC -> "Legutóbbi kóstolás"
}

// ──────────────────────────────────────────────────────────────────────────────
// WineList – LazyColumn
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun WineList(
    wines: List<WineWithStats>,
    onCardClick: (String) -> Unit,
    onCardLongPress: (Wine) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(wines, key = { it.wine.id }) { item ->
            WineCard(
                item = item,
                onClick = { onCardClick(item.wine.id) },
                onLongPress = { onCardLongPress(item.wine) },
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// WineCard – Kártyakompozíció
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun WineCard(
    item: WineWithStats,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongPress),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Accent csík (szín alapján)
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = item.wine.colour.colorHex().toComposeColor(),
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    ),
            )

            // Tartalom
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                // Sor 1: Bornév + rating badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = item.wine.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))

                    if (item.latestRating != null && item.tastingCount > 0) {
                        RatingBadge(rating = item.latestRating, tastingCount = item.tastingCount)
                    } else {
                        Text(
                            text = "Kóstolás nélkül",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Sor 2: Termelő · év · ország
                Text(
                    text = "${item.wine.producer} · ${item.wine.year} · ${item.wine.country}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Sor 3: Szőlőfajta · régió · szín
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "🍇 ${if (item.wine.isCuvee) "Cuvée" else item.wine.grape}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "${item.wine.region} · ${item.wine.colour.displayName()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingBadge(rating: Int, tastingCount: Int) {
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
                text = "★ $rating · ${tastingCount}×",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Placeholder komponensek
// ──────────────────────────────────────────────────────────────────────────────

@Composable
fun WineListEmptyContent(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = Icons.Outlined.WineBar,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            modifier = Modifier.size(64.dp),
        )
        Text(
            text = "Még nincs bor a pincédben",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Hozzáadj egy bort, és kezdd el építeni\na pincéd!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(4.dp))
        OutlinedButton(onClick = onAddClick) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text("Első bor hozzáadása")
        }
    }
}

@Composable
fun WineListNoResultsContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Icon(
            imageVector = Icons.Outlined.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = "Nincs találat",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Próbálj más keresési feltételeket vagy szűrőket.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun WineListLoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Helper extensions
// ──────────────────────────────────────────────────────────────────────────────

fun WineColour.displayName(): String = when (this) {
    WineColour.GRAY -> "Szürke"
    WineColour.ORANGE -> "Narancs"
    WineColour.WHITE -> "Fehér"
    WineColour.YELLOW -> "Sárga"
    WineColour.ROSE -> "Rozé"
    WineColour.SHILLER -> "Rotgold"
    WineColour.TAWNY -> "Tawny"
    WineColour.RED -> "Vörös"
}

fun WineColour.colorHex(): String = when (this) {
    WineColour.GRAY -> "#A0AAB4"
    WineColour.ORANGE -> "#E8A041"
    WineColour.WHITE -> "#F9E5A0"
    WineColour.YELLOW -> "#E8C441"
    WineColour.ROSE -> "#E8A0B4"
    WineColour.SHILLER -> "#D08050"
    WineColour.TAWNY -> "#B8860B"
    WineColour.RED -> "#8B2C2C"
}

private fun String.toComposeColor(): Color = Color(this.toColorInt())


