package hu.toliver.vinotes.ui.screen.stats

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.toliver.vinotes.domain.model.FullStatsData
import hu.toliver.vinotes.ui.screen.stats.charts.*
import hu.toliver.vinotes.ui.screen.stats.components.*
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.res.stringResource
import hu.toliver.vinotes.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val playedSections = remember { mutableStateMapOf<String, Boolean>() }
    fun shouldPlay(id: String) = playedSections[id] != true
    fun markPlayed(id: String) { playedSections[id] = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.statistics), style = typography.titleLarge) },
                colors = topAppBarColors(containerColor = Transparent),
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }

            state.data == null -> Box(Modifier.padding(innerPadding)) {
                Text(
                    state.errorMessage ?: stringResource(R.string.could_not_load)
                )
            }

            else -> state.data?.let { data ->
                StatsContent(
                    state = state,
                    data = data,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier.padding(innerPadding),
                    shouldPlay = ::shouldPlay,
                    markPlayed = ::markPlayed,
                )
            }
        }
    }
}

@Composable
private fun StatsContent(
    state: StatsState,
    data: FullStatsData,
    onEvent: (StatsEvent) -> Unit,
    modifier: Modifier = Modifier,
    shouldPlay: (String) -> Boolean,
    markPlayed: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item { HeaderCountersRow(data = data, playAnimations = shouldPlay("header"), onPlayed = { markPlayed("header") }) }
        item { StreakSection(data = data) }

        item {
            StatPeriodSelector(
                selected = state.period,
                onSelect = { onEvent(StatsEvent.PeriodChanged(it)) })
        }
        item {
            PeriodStatsRow(tastings = data.tastingsInPeriod, avgRating = data.avgRatingInPeriod, playAnimations = shouldPlay("period"), onPlayed = { markPlayed("period") })
        }

        item {
            SectionCard(title = stringResource(R.string.tasting_activity)) {
                TastingHeatmap(
                    tastingsByDay = data.tastingsByDay,
                    selectedDay = state.selectedDay,
                    onDayTapped = { day, count ->
                        onEvent(
                            StatsEvent.HeatmapDayTapped(
                                day,
                                count
                            )
                        )
                    },
                )
                AnimatedVisibility(visible = state.selectedDay != null) {
                    state.selectedDay?.let { day ->
                        HeatmapTooltip(
                            day = day,
                            count = state.selectedDayCount,
                            onDismiss = { onEvent(StatsEvent.HeatmapSelectionCleared) })
                    }
                }
            }
        }

        item {
            SectionCard(title = stringResource(R.string.ratings_distribution)) {
                RatingDistributionChart(buckets = data.ratingBuckets, playAnimations = shouldPlay("ratings"), onPlayed = { markPlayed("ratings") })
            }
        }

        if (data.colourDistribution.isNotEmpty()) {
            item {
                SectionCard(title = stringResource(R.string.wines_by_colour)) {
                    ColourDonutChart(distribution = data.colourDistribution, playAnimations = shouldPlay("donut"), onPlayed = { markPlayed("donut") })
                }
            }
        }

        if (data.topRegions.isNotEmpty()) {
            item {
                SectionCard(title = stringResource(R.string.top_regions)) {
                    TopRegionsChart(regions = data.topRegions, playAnimations = shouldPlay("regions"), onPlayed = { markPlayed("regions") })
                }
            }
        }

        if (data.vintageDistribution.isNotEmpty()) {
            item {
                SectionCard(title = stringResource(R.string.years)) {
                    VintageBarChart(distribution = data.vintageDistribution, playAnimations = shouldPlay("vintage"), onPlayed = { markPlayed("vintage") })
                }
            }
        }

        item { Spacer(Modifier.height(16.dp)) }
    }
}