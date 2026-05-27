package hu.toliver.vinotes.ui.screen.winelist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.WineWithStats
import hu.toliver.vinotes.domain.usecases.wine.DeleteWineUseCase
import hu.toliver.vinotes.domain.usecases.wine.GetWinesWithStatsUseCase
import hu.toliver.vinotes.domain.usecases.wine.InsertWineUseCase
import hu.toliver.vinotes.domain.usecases.wine.UpdateWineUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WineListViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val getWinesWithStats: GetWinesWithStatsUseCase,
    private val addWine: InsertWineUseCase,
    private val updateWine: UpdateWineUseCase,
    private val deleteWine: DeleteWineUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(WineListState())
    val state: StateFlow<WineListState> = _state.asStateFlow()

    private val _effect = Channel<WineListEffect>(Channel.BUFFERED)
    val effect: Flow<WineListEffect> = _effect.receiveAsFlow()

    private var allWines: List<WineWithStats> = emptyList()

    init { onEvent(WineListEvent.LoadData) }

    fun onEvent(event: WineListEvent) {
        when (event) {
            WineListEvent.LoadData -> loadWines()

            is WineListEvent.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = event.query) }
                applyFilterAndSort()
            }

            is WineListEvent.SortOrderChanged -> {
                _state.update { it.copy(sortOrder = event.order) }
                applyFilterAndSort()
            }

            WineListEvent.FilterSheetOpened -> _state.update { it.copy(isFilterSheetOpen = true) }
            WineListEvent.FilterSheetDismissed -> _state.update { it.copy(isFilterSheetOpen = false) }

            is WineListEvent.FiltersApplied -> {
                _state.update { it.copy(activeFilters = event.filters, isFilterSheetOpen = false) }
                applyFilterAndSort()
            }

            WineListEvent.FiltersCleared -> {
                _state.update { it.copy(activeFilters = WineFilters(), isFilterSheetOpen = false) }
                applyFilterAndSort()
            }

            WineListEvent.AddWineClicked -> _state.update { it.copy(isAddSheetOpen = true) }
            WineListEvent.AddSheetDismissed -> _state.update { it.copy(isAddSheetOpen = false) }

            is WineListEvent.WineCardClicked -> viewModelScope.launch {
                _effect.send(WineListEffect.NavigateToDetail(event.wineId))
            }

            is WineListEvent.WineCardLongPressed ->
                _state.update { it.copy(editingWine = event.wine) }

            WineListEvent.EditSheetDismissed ->
                _state.update { it.copy(editingWine = null) }

            is WineListEvent.WineSaved -> viewModelScope.launch {
                runCatching {
                    if (event.isNew) addWine(event.wine) else updateWine(event.wine)
                }.onSuccess {
                    _state.update { it.copy(isAddSheetOpen = false, editingWine = null) }
                    _effect.send(
                        WineListEffect.ShowSnackbar(
                            if (event.isNew) context.getString(R.string.wine_added) else context.getString(R.string.wine_updated)
                        )
                    )
                }.onFailure { e ->
                    _effect.send(WineListEffect.ShowSnackbar(e.message ?: context.getString(R.string.an_error_occurred)))
                }
            }

            is WineListEvent.WineDeleteRequested -> {
                // The UI will show a confirmation dialogue, then send WineDeleteConfirmed
            }

            is WineListEvent.WineDeleteConfirmed -> viewModelScope.launch {
                runCatching { deleteWine(event.wine) }
                    .onSuccess {
                        _state.update { it.copy(editingWine = null) }
                        _effect.send(WineListEffect.ShowSnackbar(context.getString(R.string.wine_deleted)))
                    }
                    .onFailure { e ->
                        _effect.send(WineListEffect.ShowSnackbar(e.message ?: context.getString(R.string.error_on_delete)))
                    }
            }
        }
    }

    private fun loadWines() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getWinesWithStats()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { wines ->
                    allWines = wines
                    _state.update { it.copy(isLoading = false) }
                    applyFilterAndSort()
                }
        }
    }

    private fun applyFilterAndSort() {
        val s = _state.value
        val filtered = allWines
            .filter { item -> matchesSearch(item, s.searchQuery) }
            .filter { item -> matchesFilters(item, s.activeFilters) }
            .sortedWith(comparatorFor(s.sortOrder))

        _state.update { it.copy(wines = filtered) }
    }

    private fun matchesSearch(item: WineWithStats, query: String): Boolean {
        if (query.isBlank()) return true
        val q = query.trim().lowercase()
        return item.wine.name.lowercase().contains(q)
            || item.wine.producer.lowercase().contains(q)
    }

    private fun matchesFilters(item: WineWithStats, f: WineFilters): Boolean {
        if (f.colours.isNotEmpty() && item.wine.colour !in f.colours) return false
        if (f.countries.isNotEmpty() && item.wine.country !in f.countries) return false
        f.yearFrom?.let { if (item.wine.year < it) return false }
        f.yearTo?.let { if (item.wine.year > it) return false }
        return true
    }

    private fun comparatorFor(order: WineSortOrder): Comparator<WineWithStats> =
        when (order) {
            WineSortOrder.NAME_ASC -> compareBy { it.wine.name.lowercase() }
            WineSortOrder.RATING_DESC -> compareByDescending { it.latestRating ?: -1 }
            WineSortOrder.YEAR_DESC -> compareByDescending { it.wine.year }
            WineSortOrder.TASTING_DATE_DESC -> compareByDescending { it.latestTastingDate }
        }
}
