package hu.toliver.vinotes.ui.screen.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.usecases.catalog.PerformFullImportUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveWelcomeShownUseCase
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
    @param:ApplicationContext private val context: Context,
    private val getRecentTastings: GetRecentTastingsUseCase,
    private val getDashboardStats: GetDashboardStatsUseCase,
    private val getAppPreferences: GetAppPreferencesUseCase,
    private val saveWelcomeShown: SaveWelcomeShownUseCase,
    private val performFullImport: PerformFullImportUseCase,
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
                _effect.send(DashboardEffect.NavigateToWineSelection)
            }

            DashboardEvent.SeeAllTastingsClicked -> viewModelScope.launch {
                _effect.send(DashboardEffect.NavigateToWineList)
            }

            DashboardEvent.ErrorDismissed -> {
                _state.update { it.copy(errorMessage = null) }
            }

            DashboardEvent.WelcomeDialogConfirmed -> viewModelScope.launch {
                // mark as shown immediately to avoid re-show on rotation
                _state.update { it.copy(showWelcomeDialog = false) }
                runCatching { saveWelcomeShown(true) }
                // start full import and notify user
                _state.update { it.copy(isLoading = true) }
                performFullImport()
                    .onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        _effect.send(DashboardEffect.ShowSnackbar(context.getString(R.string.full_catalog_imported)))
                    }
                    .onFailure { error ->
                        _state.update { it.copy(isLoading = false) }
                        val msg = error.message ?: context.getString(R.string.error_syncing_catalog)
                        // DNS resolution hints as in SettingsViewModel
                        if (msg.contains(context.getString(R.string.dns_resolution_failed), ignoreCase = true)
                            || msg.contains(context.getString(R.string.unable_to_resolve_host), ignoreCase = true)
                            || msg.contains(context.getString(R.string.no_address_associated_with_hostname), ignoreCase = true)) {
                            _effect.send(
                                DashboardEffect.ShowSnackbar(
                                    context.getString(R.string.cannot_resolve_catalog_host)
                                )
                            )
                        } else {
                            _effect.send(DashboardEffect.ShowSnackbar(msg))
                        }
                    }
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
                                    showWelcomeDialog = !prefs.welcomeShown,
                        )
                    }
                }.collect()

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val message = e.message ?: context.getString(R.string.unknown_error)
                _state.update {
                    it.copy(isLoading = false, errorMessage = message)
                }
                _effect.send(DashboardEffect.ShowError(message))
            }
        }
    }


}