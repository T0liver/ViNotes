package hu.toliver.vinotes.ui.screen.tastingdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.toliver.vinotes.ui.screen.tastingdetail.components.TastingDeleteConfirmDialog
import kotlinx.coroutines.flow.collectLatest
import hu.toliver.vinotes.ui.screen.tastingdetail.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TastingDetailScreen(
    tasteId: String,
    viewModel: TastingDetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(tasteId) {
        viewModel.initializeWithTasteId(tasteId)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                TastingDetailEffect.NavigateUp -> onNavigateUp()
                is TastingDetailEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    if (state.showDeleteDialog && state.taste != null) {
        TastingDeleteConfirmDialog(
            onConfirm = {
                viewModel.onEvent(TastingDetailEvent.DeleteTastingConfirmed)
            },
            onDismiss = {
                viewModel.onEvent(TastingDetailEvent.DeleteTastingDismissed)
            },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.taste != null) {
                        IconButton(onClick = { viewModel.onEvent(TastingDetailEvent.DeleteTastingClicked) }) {
                            Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            state.taste == null || state.wine == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.errorMessage ?: "Tasting not found"
                        )
                    }
                }
            }

            else -> {
                val uiData = state.taste!!.toUiData(state.wine!!)
                TastingDetailContent(
                    uiData = uiData,
                    modifier = Modifier.padding(innerPadding),
                )
            }
        }
    }
}

@Composable
fun TastingDetailContent(
    uiData: TastingDetailUiData,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        item {
            TastingHeroSection(uiData)
        }

        item {
            TastingRatingSection(uiData.rating)
        }

        item {
            Text(
                text = "Sensory Profile",
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            SensoryProfileChart(
                entries = listOf(
                    "Acidity" to uiData.acidity.second,
                    "Tannin" to uiData.tannin.second,
                    "Body" to uiData.body.second,
                    "Alcohol" to uiData.alcohol.second,
                    "Finish" to uiData.finish.second,
                ),
            )
        }

        item {
            Text(
                text = "Visual",
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    "Clarity" to uiData.clarity,
                    "Colour Intensity" to uiData.colourIntensity,
                    "Colour" to uiData.colour,
                ),
            )
        }

        if (uiData.otherVisual.isNotBlank()) {
            item {
                ParameterCard("Other Visual Observations", uiData.otherVisual)
            }
        }

        item {
            Text(
                text = "Nose",
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    "Intensity" to uiData.noseIntensity,
                    "Development" to uiData.noseDevelopment,
                ),
            )
        }

        if (uiData.noseAroma.isNotBlank()) {
            item {
                ParameterCard("Aroma Notes", uiData.noseAroma)
            }
        }

        if (uiData.otherNose.isNotBlank()) {
            item {
                ParameterCard("Other Nose Observations", uiData.otherNose)
            }
        }

        item {
            Text(
                text = "Palate",
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    "Sweetness" to uiData.sweetness,
                    "Acidity" to uiData.acidity.first,
                    "Tannin" to uiData.tannin.first,
                    "Body" to uiData.body.first,
                    "Alcohol" to uiData.alcohol.first,
                    "Flavour Intensity" to uiData.flavourIntensity,
                    "Finish" to uiData.finish.first,
                ),
            )
        }

        if (uiData.flavourCharacteristics.isNotBlank()) {
            item {
                ParameterCard("Flavour Characteristics", uiData.flavourCharacteristics)
            }
        }

        if (uiData.otherPalate.isNotBlank()) {
            item {
                ParameterCard("Other Palate Observations", uiData.otherPalate)
            }
        }

        item {
            Text(
                text = "Overall Impression",
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        if (uiData.overallImpression.isNotBlank()) {
            item {
                ParameterCard("Impression", uiData.overallImpression)
            }
        }

        if (uiData.bestWith.isNotBlank()) {
            item {
                ParameterCard("Best With", uiData.bestWith)
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (uiData.wouldDrinkAgain) colorScheme.primaryContainer
                    else colorScheme.errorContainer
                ),
            ) {
                Text(
                    text = if (uiData.wouldDrinkAgain) "✓ Would drink again" else "✗ Would not drink again",
                    style = typography.bodyMedium,
                    color = if (uiData.wouldDrinkAgain) colorScheme.onPrimaryContainer
                    else colorScheme.onErrorContainer,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                )
            }
        }

        item {
            Text(
                text = "Location & Date",
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    "Place" to uiData.place,
                    "Date" to uiData.date,
                ),
            )
        }
    }
}