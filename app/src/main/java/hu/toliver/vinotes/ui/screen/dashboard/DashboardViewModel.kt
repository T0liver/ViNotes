package hu.toliver.vinotes.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _effect = Channel<DashboardEffect>(Channel.BUFFERED)
    val effect: Flow<DashboardEffect> = _effect.receiveAsFlow()

    init {
        onEvent(DashboardEvent.LoadData)
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.LoadData,
            DashboardEvent.RefreshRequested -> {
            }

            is DashboardEvent.TastingCardClicked -> viewModelScope.launch {
                _effect.send(DashboardEffect.NavigateToDetail(event.wineId, event.tasteId))
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
}