package hu.toliver.vinotes.ui.screen.tastingdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.toliver.vinotes.domain.usecases.taste.DeleteTasteUseCase
import hu.toliver.vinotes.domain.usecases.taste.GetTasteWithWineUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TastingDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getTasteWithWine: GetTasteWithWineUseCase,
    private val deleteTaste: DeleteTasteUseCase,
) : ViewModel() {

    private var initialized = false

    private val _state = MutableStateFlow(TastingDetailState())
    val state: StateFlow<TastingDetailState> = _state.asStateFlow()

    private val _effect = Channel<TastingDetailEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun initializeWithTasteId(id: String) {
        if (initialized || id.isBlank()) return

        initialized = true
        savedStateHandle["tasteId"] = id
        onEvent(TastingDetailEvent.LoadData)
    }

    fun onEvent(event: TastingDetailEvent) {
        when (event) {
            TastingDetailEvent.LoadData -> loadData()

            TastingDetailEvent.DeleteTastingClicked ->
                _state.value = _state.value.copy(showDeleteDialog = true)

            TastingDetailEvent.DeleteTastingConfirmed -> viewModelScope.launch {
                val taste = _state.value.taste ?: return@launch
                runCatching { deleteTaste(taste) }
                    .onSuccess {
                        _state.value = _state.value.copy(showDeleteDialog = false)
                        _effect.send(TastingDetailEffect.NavigateUp)
                    }
                    .onFailure { e -> _effect.send(TastingDetailEffect.ShowSnackbar(e.message ?: "Error on delete")) }
            }

            TastingDetailEvent.DeleteTastingDismissed ->
                _state.value = _state.value.copy(showDeleteDialog = false)
        }
    }

    private fun loadData() {
        val currentTasteId = savedStateHandle.get<String>("tasteId") ?: ""
        if (currentTasteId.isEmpty()) {
            _state.value = _state.value.copy(isLoading = false, errorMessage = "Tasting ID is missing")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getTasteWithWine(currentTasteId)
                .onSuccess { tasteWithWine ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        taste = tasteWithWine.taste,
                        wine = tasteWithWine.wine,
                    )
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "The tasting could not be found."
                    )
                }
        }
    }
}