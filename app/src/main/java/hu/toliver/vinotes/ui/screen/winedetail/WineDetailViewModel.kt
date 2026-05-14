package hu.toliver.vinotes.ui.screen.winedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.toliver.vinotes.data.local.converters.EnumConverter.toFloat
import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.usecases.wine.DeleteWineUseCase
import hu.toliver.vinotes.domain.usecases.wine.GetWineWithTastingsUseCase
import hu.toliver.vinotes.domain.usecases.wine.UpdateWineUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WineDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getWineWithTastings: GetWineWithTastingsUseCase,
    private val updateWine: UpdateWineUseCase,
    private val deleteWine: DeleteWineUseCase,
) : ViewModel() {

    private val wineId: String = savedStateHandle.get<String>("wineId") ?: ""

    private var initialized = false

    private val _state = MutableStateFlow(WineDetailState())
    val state: StateFlow<WineDetailState> = _state.asStateFlow()

    private val _effect = Channel<WineDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun initializeWithWineId(id: String) {
        if (!initialized && id.isNotEmpty()) {
            initialized = true
            savedStateHandle["wineId"] = id
            onEvent(WineDetailEvent.LoadData)
        }
    }

    fun onEvent(event: WineDetailEvent) {
        when (event) {
            WineDetailEvent.LoadData -> loadData()

            WineDetailEvent.EditWineClicked -> _state.value = _state.value.copy(isEditSheetOpen = true)
            WineDetailEvent.EditSheetDismissed -> _state.value = _state.value.copy(isEditSheetOpen = false)

            is WineDetailEvent.WineSaved -> viewModelScope.launch {
                runCatching { updateWine(event.wine) }
                    .onSuccess {
                        _state.value = _state.value.copy(isEditSheetOpen = false)
                        _effect.send(WineDetailEffect.ShowSnackbar("Wine updated"))
                    }
                    .onFailure { e ->
                        _effect.send(WineDetailEffect.ShowSnackbar(e.message ?: "Error on save"))
                    }
            }

            WineDetailEvent.DeleteWineClicked ->
                _state.value = _state.value.copy(showDeleteWineDialog = true)

            WineDetailEvent.DeleteWineDismissed ->
                _state.value = _state.value.copy(showDeleteWineDialog = false)

            WineDetailEvent.DeleteWineConfirmed -> viewModelScope.launch {
                val wine = _state.value.wine ?: return@launch
                runCatching { deleteWine(wine) }
                    .onSuccess { _effect.send(WineDetailEffect.NavigateUp) }
                    .onFailure { e -> _effect.send(WineDetailEffect.ShowSnackbar(e.message ?: "Error on delete")) }
            }

            WineDetailEvent.AddTastingClicked -> viewModelScope.launch {
                val currentWineId = savedStateHandle.get<String>("wineId") ?: ""
                _effect.send(WineDetailEffect.NavigateToAddTasting(currentWineId))
            }

            is WineDetailEvent.TastingClicked -> {
                /* TODO: TasteDetail screen later */
            }
        }
    }

    private fun loadData() {
        val currentWineId = savedStateHandle.get<String>("wineId") ?: ""
        if (currentWineId.isEmpty()) {
            _state.value = _state.value.copy(isLoading = false, errorMessage = "Wine ID is missing")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getWineWithTastings(currentWineId)
                .catch { e ->
                    _state.value = _state.value.copy(isLoading = false, errorMessage = e.message)
                }
                .collect { result ->
                    if (result == null) {
                        _state.value = _state.value.copy(isLoading = false, errorMessage = "The wine could not be found.")
                        return@collect
                    }
                    _state.value = _state.value.copy(
                        isLoading = false,
                        wine = result.wine,
                        tastings = result.tastings,
                        radarData = result.tastings.toRadarData(),
                    )
                }
        }
    }
}

private fun List<Taste>.toRadarData(): RadarData? {
    if (isEmpty()) return null
    return RadarData(
        acidity = map { it.acidity.toFloat() }.average().toFloat(),
        tannin = map { it.tannin.toFloat() }.average().toFloat(),
        body = map { it.body.toFloat() }.average().toFloat(),
        alcohol = map { it.alcohol.toFloat() }.average().toFloat(),
        finish = map { it.finish.toFloat() }.average().toFloat(),
    )
}

