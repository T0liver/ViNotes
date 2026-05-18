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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.toliver.vinotes.R
import hu.toliver.vinotes.ui.screen.tastingdetail.components.ParameterCard
import hu.toliver.vinotes.ui.screen.tastingdetail.components.SensoryProfileChart
import hu.toliver.vinotes.ui.screen.tastingdetail.components.TastingDeleteConfirmDialog
import hu.toliver.vinotes.ui.screen.tastingdetail.components.TastingDetailUiData
import hu.toliver.vinotes.ui.screen.tastingdetail.components.TastingHeroSection
import hu.toliver.vinotes.ui.screen.tastingdetail.components.TastingParametersCard
import hu.toliver.vinotes.ui.screen.tastingdetail.components.TastingRatingSection
import hu.toliver.vinotes.ui.screen.tastingdetail.components.toUiData
import kotlinx.coroutines.flow.collectLatest

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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    if (state.taste != null) {
                        IconButton(onClick = { viewModel.onEvent(TastingDetailEvent.DeleteTastingClicked) }) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
    ) { innerPadding ->
        val context = LocalContext.current
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
                            text = state.errorMessage ?: stringResource(R.string.tasting_not_found)
                        )
                    }
                }
            }

            else -> {
                val uiData = state.taste!!.toUiData(state.wine!!, context)
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
                text = stringResource(R.string.sensory_profile),
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            SensoryProfileChart(
                entries = listOf(
                    R.string.acidity.toString() to uiData.acidity.second,
                    R.string.tannin.toString() to uiData.tannin.second,
                    R.string.body.toString() to uiData.body.second,
                    R.string.alcohol.toString() to uiData.alcohol.second,
                    R.string.finish.toString() to uiData.finish.second,
                ),
            )
        }

        item {
            Text(
                text = stringResource(R.string.visual),
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    stringResource(R.string.clarity) to uiData.clarity,
                    stringResource(R.string.colour_intensity) to uiData.colourIntensity,
                    stringResource(R.string.colour) to uiData.colour,
                ),
            )
        }

        if (uiData.otherVisual.isNotBlank()) {
            item {
                ParameterCard(
                    stringResource(R.string.other_visual_observations), uiData.otherVisual
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.nose),
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    stringResource(R.string.intensity) to uiData.noseIntensity,
                    stringResource(R.string.development) to uiData.noseDevelopment,
                ),
            )
        }

        if (uiData.noseAroma.isNotBlank()) {
            item {
                ParameterCard(stringResource(R.string.aroma_notes), uiData.noseAroma)
            }
        }

        if (uiData.otherNose.isNotBlank()) {
            item {
                ParameterCard(stringResource(R.string.other_nose_observations), uiData.otherNose)
            }
        }

        item {
            Text(
                text = stringResource(R.string.palate),
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    stringResource(R.string.sweetness) to uiData.sweetness,
                    stringResource(R.string.acidity) to uiData.acidity.first,
                    stringResource(R.string.tannin) to uiData.tannin.first,
                    stringResource(R.string.body) to uiData.body.first,
                    stringResource(R.string.alcohol) to uiData.alcohol.first,
                    stringResource(R.string.flavour_intensity) to uiData.flavourIntensity,
                    stringResource(R.string.finish) to uiData.finish.first,
                ),
            )
        }

        if (uiData.flavourCharacteristics.isNotBlank()) {
            item {
                ParameterCard(
                    stringResource(R.string.flavour_characteristics), uiData.flavourCharacteristics
                )
            }
        }

        if (uiData.otherPalate.isNotBlank()) {
            item {
                ParameterCard(
                    stringResource(R.string.other_palate_observations), uiData.otherPalate
                )
            }
        }

        item {
            Text(
                text = stringResource(R.string.overall_impression),
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        if (uiData.overallImpression.isNotBlank()) {
            item {
                ParameterCard(stringResource(R.string.impression), uiData.overallImpression)
            }
        }

        if (uiData.bestWith.isNotBlank()) {
            item {
                ParameterCard(stringResource(R.string.best_with), uiData.bestWith)
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
                    text = if (uiData.wouldDrinkAgain) stringResource(R.string.would_drink_again_tick) else stringResource(
                        R.string.would_not_drink_again_tick
                    ),
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
                text = stringResource(R.string.location_and_date),
                style = typography.headlineSmall,
                color = colorScheme.onBackground,
            )
        }

        item {
            TastingParametersCard(
                parameters = listOf(
                    stringResource(R.string.place) to uiData.place,
                    stringResource(R.string.date) to uiData.date,
                ),
            )
        }
    }
}