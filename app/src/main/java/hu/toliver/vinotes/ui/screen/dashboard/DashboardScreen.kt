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
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.toliver.vinotes.ui.screen.dashboard.components.DashboardHeader
import hu.toliver.vinotes.ui.screen.dashboard.components.EmptyTastingsPlaceholder
import hu.toliver.vinotes.ui.screen.dashboard.components.QuickStatsRow
import hu.toliver.vinotes.ui.screen.dashboard.components.RecentTastingCard
import hu.toliver.vinotes.ui.screen.dashboard.components.SectionHeader
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToWineList: () -> Unit,
    onNavigateToTastingDetail: (tasteId: String) -> Unit,
    onNavigateToAddTasting: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.NavigateToTastingDetail -> {
                    onNavigateToTastingDetail(effect.tasteId)
                }

                is DashboardEffect.NavigateToWineDetail -> {
                    Unit
                }

                DashboardEffect.NavigateToAddTasting -> {
                    onNavigateToAddTasting()
                }

                DashboardEffect.NavigateToWineList -> {
                    onNavigateToWineList()
                }

                is DashboardEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    DashboardHeader(username = state.username)
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = colorScheme.onBackground,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
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
                        onEvent(DashboardEvent.TastingCardClicked(tasting.tasteId))
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