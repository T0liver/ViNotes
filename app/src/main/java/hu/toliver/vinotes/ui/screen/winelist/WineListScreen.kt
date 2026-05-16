package hu.toliver.vinotes.ui.screen.winelist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.ui.screen.winelist.components.DeleteConfirmDialog
import hu.toliver.vinotes.ui.screen.winelist.components.WineFilterSheet
import hu.toliver.vinotes.ui.screen.winelist.components.WineFormSheet
import hu.toliver.vinotes.ui.screen.winelist.components.WineList
import hu.toliver.vinotes.ui.screen.winelist.components.WineListEmptyContent
import hu.toliver.vinotes.ui.screen.winelist.components.WineListLoadingContent
import hu.toliver.vinotes.ui.screen.winelist.components.WineListNoResultsContent
import hu.toliver.vinotes.ui.screen.winelist.components.WineSearchBar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WineListScreen(
    viewModel: WineListViewModel = hiltViewModel(),
    onNavigateToDetail: (wineId: String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDeleteDialog by remember { mutableStateOf<Wine?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WineListEffect.NavigateToDetail -> onNavigateToDetail(effect.wineId)
                is WineListEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    showDeleteDialog?.let { wine ->
        DeleteConfirmDialog(
            wineName = wine.name,
            onConfirm = {
                viewModel.onEvent(WineListEvent.WineDeleteConfirmed(wine))
                showDeleteDialog = null
            },
            onDismiss = { showDeleteDialog = null },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(WineListEvent.AddWineClicked) }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Wine")
            }
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            WineSearchBar(
                query = state.searchQuery,
                onQueryChange = { viewModel.onEvent(WineListEvent.SearchQueryChanged(it)) },
                sortOrder = state.sortOrder,
                onSortChange = { viewModel.onEvent(WineListEvent.SortOrderChanged(it)) },
                filterActive = state.activeFilters.isActive,
                onFilterClick = { viewModel.onEvent(WineListEvent.FilterSheetOpened) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            when {
                state.isLoading -> WineListLoadingContent()
                state.wines.isEmpty() && state.searchQuery.isBlank() && !state.activeFilters.isActive ->
                    WineListEmptyContent(onAddClick = { viewModel.onEvent(WineListEvent.AddWineClicked) })
                state.wines.isEmpty() ->
                    WineListNoResultsContent()
                else ->
                    WineList(
                        wines = state.wines,
                        onCardClick = { viewModel.onEvent(WineListEvent.WineCardClicked(it)) },
                        onCardLongPress = { viewModel.onEvent(WineListEvent.WineCardLongPressed(it)) },
                    )
            }
        }
    }

    if (state.isFilterSheetOpen) {
        WineFilterSheet(
            currentFilters = state.activeFilters,
            availableColours = WineColour.entries,
            availableCountries = state.wines
                .map { it.wine.country }
                .distinct()
                .sorted(),
            onApply = { viewModel.onEvent(WineListEvent.FiltersApplied(it)) },
            onClear = { viewModel.onEvent(WineListEvent.FiltersCleared) },
            onDismiss = { viewModel.onEvent(WineListEvent.FilterSheetDismissed) },
        )
    }

    if (state.isAddSheetOpen || state.editingWine != null) {
        WineFormSheet(
            editingWine = state.editingWine,
            onSave = { wine, isNew ->
                viewModel.onEvent(WineListEvent.WineSaved(wine, isNew))
            },
            onDelete = { wine ->
                showDeleteDialog = wine
            },
            onDismiss = {
                if (state.isAddSheetOpen) {
                    viewModel.onEvent(WineListEvent.AddSheetDismissed)
                } else {
                    viewModel.onEvent(WineListEvent.EditSheetDismissed)
                }
            },
        )
    }
}
