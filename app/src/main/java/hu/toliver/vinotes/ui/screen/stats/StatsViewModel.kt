package hu.toliver.vinotes.ui.screen.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.toliver.vinotes.domain.usecases.stats.GetFullStatsUseCase
import hu.toliver.vinotes.domain.usecases.stats.StatPeriod
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getFullStats: GetFullStatsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(StatsState())
    val state: StateFlow<StatsState> = _state.asStateFlow()

    private val _effect = Channel<StatsEffect>(Channel.BUFFERED)
    val effect: Flow<StatsEffect> = _effect.receiveAsFlow()

    init {
        onEvent(StatsEvent.LoadData)
    }

    fun onEvent(event: StatsEvent) {
        when (event) {
            StatsEvent.LoadData -> loadStats(_state.value.period)

            is StatsEvent.PeriodChanged -> {
                _state.update { it.copy(period = event.period) }
                loadStats(event.period)
            }

            is StatsEvent.HeatmapDayTapped -> {
                _state.update {
                    it.copy(selectedDay = event.day, selectedDayCount = event.count)
                }
            }

            StatsEvent.HeatmapSelectionCleared -> {
                _state.update { it.copy(selectedDay = null, selectedDayCount = 0) }
            }
        }
    }

    private fun loadStats(period: StatPeriod) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            getFullStats(period)
                .catch { e ->
                    _state.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { data ->
                    _state.update { it.copy(isLoading = false, data = data) }
                }
        }
    }
}

