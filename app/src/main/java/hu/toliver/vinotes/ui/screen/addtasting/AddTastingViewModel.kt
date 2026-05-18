package hu.toliver.vinotes.ui.screen.addtasting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.usecases.location.GetCurrentLocationUseCase
import hu.toliver.vinotes.domain.usecases.location.ReverseGeocodeUseCase
import hu.toliver.vinotes.domain.usecases.taste.AddTasteUseCase
import hu.toliver.vinotes.domain.usecases.taste.GetTasteByIdUseCase
import hu.toliver.vinotes.domain.usecases.taste.UpdateTasteUseCase
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddTastingViewModel @Inject constructor(
    private val addTaste: AddTasteUseCase,
    private val updateTaste: UpdateTasteUseCase,
    private val getTasteById: GetTasteByIdUseCase,
    private val getCurrentLocation: GetCurrentLocationUseCase,
    private val reverseGeocode: ReverseGeocodeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AddTastingState())
    val state: StateFlow<AddTastingState> = _state.asStateFlow()

    private val _effect = Channel<AddTastingEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    val stepCount = 4

    fun onEvent(event: AddTastingEvent) {
        when (event) {

            is AddTastingEvent.Init -> {
                _state.value = _state.value.copy(wineId = event.wineId)
                event.editTasteId?.let { loadForEdit(it) }
            }

            AddTastingEvent.NextStep -> {
                val current = _state.value.currentStep
                if (current < stepCount - 1)
                    _state.value = _state.value.copy(currentStep = current + 1)
            }

            AddTastingEvent.PrevStep -> {
                val current = _state.value.currentStep
                if (current > 0)
                    _state.value = _state.value.copy(currentStep = current - 1)
            }

            is AddTastingEvent.ClarityChanged -> _state.value = _state.value.copy(clarity = event.v)
            is AddTastingEvent.ColourIntensityChanged -> _state.value = _state.value.copy(colourIntensity = event.v)
            is AddTastingEvent.ColourChanged -> _state.value = _state.value.copy(colour = event.v)
            is AddTastingEvent.OtherVisualChanged -> _state.value = _state.value.copy(otherVisual = event.v)
            is AddTastingEvent.NoseIntensityChanged -> _state.value = _state.value.copy(noseIntensity = event.v)
            is AddTastingEvent.NoseAromaChanged -> _state.value = _state.value.copy(noseAroma = event.v)
            is AddTastingEvent.NoseDevelopmentChanged -> _state.value = _state.value.copy(noseDevelopment = event.v)
            is AddTastingEvent.OtherNoseChanged -> _state.value = _state.value.copy(otherNose = event.v)
            is AddTastingEvent.SweetnessChanged -> _state.value = _state.value.copy(sweetness = event.v)
            is AddTastingEvent.AcidityChanged -> _state.value = _state.value.copy(acidity = event.v)
            is AddTastingEvent.TanninChanged -> _state.value = _state.value.copy(tannin = event.v)
            is AddTastingEvent.BodyChanged -> _state.value = _state.value.copy(body = event.v)
            is AddTastingEvent.AlcoholChanged -> _state.value = _state.value.copy(alcohol = event.v)
            is AddTastingEvent.FlavourIntensityChanged -> _state.value = _state.value.copy(flavourIntensity = event.v)
            is AddTastingEvent.FlavourCharacteristicsChanged -> _state.value = _state.value.copy(flavourCharacteristics = event.v)
            is AddTastingEvent.FinishChanged -> _state.value = _state.value.copy(finish = event.v)
            is AddTastingEvent.OtherPalateChanged -> _state.value = _state.value.copy(otherPalate = event.v)
            is AddTastingEvent.RatingChanged -> _state.value = _state.value.copy(rating = event.v)
            is AddTastingEvent.OverallImpressionChanged -> _state.value = _state.value.copy(overallImpression = event.v)
            is AddTastingEvent.BestWithChanged -> _state.value = _state.value.copy(bestWith = event.v)
            is AddTastingEvent.WouldDrinkAgainChanged -> _state.value = _state.value.copy(wouldDrinkAgain = event.v)
            is AddTastingEvent.DateChanged -> _state.value = _state.value.copy(date = event.v)
            is AddTastingEvent.PlaceChanged -> _state.value = _state.value.copy(place = event.v)

            AddTastingEvent.OnFetchLocationClicked -> {
                _state.update { it.copy(locationError = null) }
                viewModelScope.launch {
                    _effect.send(AddTastingEffect.RequestLocationPermission)
                }
            }

            AddTastingEvent.OnLocationPermissionGranted -> fetchLocation()
            AddTastingEvent.OnLocationPermissionDenied -> {
                viewModelScope.launch {
                    emitLocationError("Needs location permission to fetch current location")
                }
            }

            AddTastingEvent.SaveTasting -> saveTasting()
        }
    }

    private fun fetchLocation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoadingLocation = true, locationError = null) }

            try {
                withTimeout(15_000L) {
                    getCurrentLocation()
                        .onSuccess { (latitude, longitude) ->
                            reverseGeocode(latitude, longitude)
                                .onSuccess { place ->
                                    _state.update {
                                        it.copy(
                                            isLoadingLocation = false,
                                            place = place,
                                            latitude = latitude,
                                            longitude = longitude,
                                            locationError = null,
                                        )
                                    }
                                }
                                .onFailure { error ->
                                    _state.update {
                                        it.copy(
                                            isLoadingLocation = false,
                                            latitude = latitude,
                                            longitude = longitude,
                                        )
                                    }
                                    emitLocationError(error.message ?: "Location fetched but reverse geocoding failed")
                                }
                        }
                        .onFailure { error ->
                            _state.update { it.copy(isLoadingLocation = false) }
                            emitLocationError(error.message ?: "Failed to fetch location")
                        }
                }
            } catch (_: TimeoutCancellationException) {
                _state.update { it.copy(isLoadingLocation = false) }
                emitLocationError("Location fetch timed out. Please try again.")
            }
        }
    }

    private suspend fun emitLocationError(message: String) {
        _state.update { it.copy(locationError = message, isLoadingLocation = false) }
        _effect.send(AddTastingEffect.ShowLocationError(message))
    }

    private fun loadForEdit(tasteId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = getTasteById(tasteId)
            result.onSuccess { taste ->
                _state.value = _state.value.copy(
                    isLoading = false,
                    isEditMode = true,
                    editingTasteId = tasteId,
                    clarity = taste.clarity,
                    colourIntensity = taste.colourIntensity,
                    colour = taste.colour,
                    otherVisual = taste.otherVisual,
                    noseIntensity = taste.noseIntensity,
                    noseAroma = taste.noseAroma,
                    noseDevelopment = taste.noseDevelopment,
                    otherNose = taste.otherNose,
                    sweetness = taste.sweetness,
                    acidity = taste.acidity,
                    tannin = taste.tannin,
                    body = taste.body,
                    alcohol = taste.alcohol,
                    flavourIntensity = taste.flavourIntensity,
                    flavourCharacteristics = taste.flavourCharacteristics,
                    finish = taste.finish,
                    otherPalate = taste.otherPalate,
                    rating = taste.rating,
                    overallImpression = taste.overallImpression,
                    bestWith = taste.bestWith,
                    wouldDrinkAgain = taste.wouldDrinkAgain,
                    date = taste.date,
                    place = taste.place,
                    latitude = taste.latitude,
                    longitude = taste.longitude,
                )
            }
            result.onFailure { e ->
                _state.value = _state.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    private fun saveTasting() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            val s = _state.value
            if (s.wineId.isBlank()) {
                _state.value = _state.value.copy(isSaving = false)
                _effect.send(AddTastingEffect.ShowSnackbar("Please select a wine first"))
                return@launch
            }
            val taste = Taste(
                id = s.editingTasteId ?: UUID.randomUUID().toString(),
                clarity = s.clarity,
                colourIntensity = s.colourIntensity,
                colour = s.colour,
                otherVisual = s.otherVisual,
                noseIntensity = s.noseIntensity,
                noseAroma = s.noseAroma,
                noseDevelopment = s.noseDevelopment,
                otherNose = s.otherNose,
                sweetness = s.sweetness,
                acidity = s.acidity,
                tannin = s.tannin,
                body = s.body,
                alcohol = s.alcohol,
                flavourIntensity = s.flavourIntensity,
                flavourCharacteristics = s.flavourCharacteristics,
                finish = s.finish,
                otherPalate = s.otherPalate,
                rating = s.rating,
                overallImpression = s.overallImpression,
                bestWith = s.bestWith,
                wouldDrinkAgain = s.wouldDrinkAgain,
                date = s.date,
                wineId = s.wineId,
                place = s.place,
                latitude = s.latitude,
                longitude = s.longitude,
            )
            val result = if (s.isEditMode) updateTaste(taste) else addTaste(taste)
            result
                .onSuccess {
                    _state.value = _state.value.copy(isSaving = false)
                    viewModelScope.launch { _effect.send(AddTastingEffect.NavigateUp) }
                }
                .onFailure { e ->
                    _state.value = _state.value.copy(isSaving = false)
                    viewModelScope.launch { _effect.send(AddTastingEffect.ShowSnackbar(e.message ?: "Error on save")) }
                }
        }
    }
}
