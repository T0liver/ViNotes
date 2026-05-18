package hu.toliver.vinotes.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import hu.toliver.vinotes.domain.usecases.stats.GetDashboardStatsUseCase
import hu.toliver.vinotes.domain.usecases.taste.GetRecentTastingsUseCase
import hu.toliver.vinotes.domain.usecases.settings.GetAppPreferencesUseCase
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getRecentTastings: GetRecentTastingsUseCase,
    private val getDashboardStats: GetDashboardStatsUseCase,
    private val getAppPreferences: GetAppPreferencesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = Channel<DashboardEffect>(Channel.BUFFERED)
    val effect: Flow<DashboardEffect> = _effect.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        onEvent(DashboardEvent.LoadData)
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.LoadData,
            DashboardEvent.RefreshRequested -> loadDashboard()

            is DashboardEvent.TastingCardClicked -> viewModelScope.launch {
                _effect.send(DashboardEffect.NavigateToTastingDetail(event.tasteId))
            }

            DashboardEvent.AddTastingClicked -> viewModelScope.launch {
                _effect.send(DashboardEffect.NavigateToAddTasting)
            }

            DashboardEvent.SeeAllTastingsClicked -> viewModelScope.launch {
                _effect.send(DashboardEffect.NavigateToWineList)
            }

            DashboardEvent.ErrorDismissed -> {
                _state.update { it.copy(errorMessage = null) }
            }
        }
    }

    private fun loadDashboard() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                combine(
                    getRecentTastings(limit = 5),
                    getDashboardStats(),
                    getAppPreferences(),
                ) { tastings, stats, prefs ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            recentTastings = tastings.map { t -> t.toRecentItem() },
                            totalWines = stats.totalWines,
                            totalTastings = stats.totalTastings,
                            averageRating = stats.averageRating,
                            topRegion = stats.topRegion,
                            username = prefs.username,
                        )
                    }
                }.collect()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Unknown error")
                }
                _effect.send(DashboardEffect.ShowError(e.message ?: "Unknown error"))
            }
        }
    }
}
