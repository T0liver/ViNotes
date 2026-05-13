package hu.toliver.vinotes.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun DashboardScreen(
    viewModel:             DashboardViewModel = hiltViewModel(),
    onNavigateToWineList:  () -> Unit,
    onNavigateToDetail:    (wineId: String, tasteId: String) -> Unit,
    onNavigateToAddTasting: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.NavigateToDetail ->
                    onNavigateToDetail(effect.wineId, effect.tasteId)

                DashboardEffect.NavigateToAddTasting ->
                    onNavigateToAddTasting()

                DashboardEffect.NavigateToWineList ->
                    onNavigateToWineList()

                is DashboardEffect.ShowError ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text    = { Text("Taste") },
                icon    = { Icon(Icons.Filled.Add, contentDescription = null) },
                onClick = { viewModel.onEvent(DashboardEvent.AddTastingClicked) },
            )
        },
    ) { innerPadding ->
        val pullState = rememberPullToRefreshState()
        PullToRefreshBox(
            isRefreshing = state.isLoading,
            onRefresh    = { viewModel.onEvent(DashboardEvent.RefreshRequested) },
            state        = pullState,
            modifier     = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            when {
                state.isLoading && state.recentTastings.isEmpty() -> DashboardLoadingContent()
                else -> DashboardContent(
                    state    = state,
                    onEvent  = viewModel::onEvent,
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    state:   DashboardState,
    onEvent: (DashboardEvent) -> Unit,
) {
    LazyColumn(
        contentPadding     = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        item {
            //DashboardHeader()               // TODO: implement
        }

        item {
            /*
            QuickStatsRow(
                totalWines    = state.totalWines,
                totalTastings = state.totalTastings,
                averageRating = state.averageRating,
                topRegion     = state.topRegion,
            )                               // TODO: implement
             */
        }

        item {
            /*
            SectionHeader(
                title    = "Last Tastings",
                actionLabel = "All",
                onAction = { onEvent(DashboardEvent.SeeAllTastingsClicked) },
            )                               // TODO: implement
             */
        }

        if (state.recentTastings.isEmpty()) {
            item {
                /*
                EmptyTastingsPlaceholder(   // TODO: implement
                    onAddClick = { onEvent(DashboardEvent.AddTastingClicked) }
                )
                 */
            }
        } else {
            items(
                items = state.recentTastings,
                key   = { it.tasteId },
            ) { _ ->
                /*
                RecentTastingCard(
                    item    = tasting,
                    onClick = {
                        onEvent(DashboardEvent.TastingCardClicked(tasting.tasteId, tasting.wineId))
                    },
                ) // TODO: implement
                 */
            }
        }
    }
}

@Composable
private fun DashboardLoadingContent() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}