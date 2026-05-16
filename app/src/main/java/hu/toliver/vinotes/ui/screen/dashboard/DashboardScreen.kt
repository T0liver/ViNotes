package hu.toliver.vinotes.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.toliver.vinotes.ui.screen.dashboard.components.DashboardHeader
import hu.toliver.vinotes.ui.screen.dashboard.components.EmptyTastingsPlaceholder
import hu.toliver.vinotes.ui.screen.dashboard.components.QuickStatsRow
import hu.toliver.vinotes.ui.screen.dashboard.components.RecentTastingCard
import hu.toliver.vinotes.ui.screen.dashboard.components.SectionHeader
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToWineList: () -> Unit,
    onNavigateToDetail: (wineId: String, tasteId: String) -> Unit,
    onNavigateToAddTasting: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.NavigateToDetail -> onNavigateToDetail(effect.wineId, effect.tasteId)
                DashboardEffect.NavigateToAddTasting -> onNavigateToAddTasting()
                DashboardEffect.NavigateToWineList -> onNavigateToWineList()
                is DashboardEffect.ShowError -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Taste") },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                onClick = { viewModel.onEvent(DashboardEvent.AddTastingClicked) },
            )
        },
    ) { innerPadding ->
        val pullState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh = { viewModel.onEvent(DashboardEvent.RefreshRequested) },
            state = pullState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                state.isLoading && state.recentTastings.isEmpty() -> DashboardLoadingContent()
                else -> DashboardContent(
                    state = state,
                    onEvent = viewModel::onEvent,
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    state: DashboardState,
    onEvent: (DashboardEvent) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            DashboardHeader()
        }

        item {
            QuickStatsRow(
                totalWines = state.totalWines,
                totalTastings = state.totalTastings,
                averageRating = state.averageRating,
                topRegion = state.topRegion,
            )
        }

        item {
            SectionHeader(
                title = "Latest Tastings",
                actionLabel = "All",
                onAction = { onEvent(DashboardEvent.SeeAllTastingsClicked) },
            )
        }

        if (state.recentTastings.isEmpty()) {
            item {
                EmptyTastingsPlaceholder(
                    onAddClick = { onEvent(DashboardEvent.AddTastingClicked) },
                )
            }
        } else {
            items(
                items = state.recentTastings,
                key = { it.tasteId },
            ) { tasting ->
                RecentTastingCard(
                    item = tasting,
                    onClick = {
                        onEvent(DashboardEvent.TastingCardClicked(tasting.tasteId, tasting.wineId))
                    },
                )
            }
        }
    }
}

@Composable
private fun DashboardLoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}