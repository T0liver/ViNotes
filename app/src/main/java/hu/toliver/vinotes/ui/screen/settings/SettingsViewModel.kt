package hu.toliver.vinotes.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.toliver.vinotes.ui.AppConstants
import hu.toliver.vinotes.domain.usecases.settings.ClearAllDataUseCase
import hu.toliver.vinotes.domain.usecases.settings.GetAppPreferencesUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveCatalogUrlUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveUsernameUseCase
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val getAppPreferences: GetAppPreferencesUseCase,
	private val saveCatalogUrl: SaveCatalogUrlUseCase,
	private val saveUsername: SaveUsernameUseCase,
	private val clearAllData: ClearAllDataUseCase,
) : ViewModel() {

	private val _state = MutableStateFlow(SettingsState())
	val state: StateFlow<SettingsState> = _state.asStateFlow()

	private val _effect = Channel<SettingsEffect>(Channel.BUFFERED)
	val effect: Flow<SettingsEffect> = _effect.receiveAsFlow()

	init {
		onEvent(SettingsEvent.LoadData)
	}

	fun onEvent(event: SettingsEvent) {
		when (event) {
			SettingsEvent.LoadData -> viewModelScope.launch {
				getAppPreferences().collect { prefs ->
					_state.update {
						it.copy(
							isLoading = false,
							catalogUrl = prefs.catalogUrl,
							username = prefs.username,
						)
					}
				}
			}

			SettingsEvent.EditUrlClicked -> _state.update {
				it.copy(showUrlEditDialog = true, editingUrlValue = it.catalogUrl)
			}

			is SettingsEvent.UrlEditChanged -> _state.update {
				it.copy(editingUrlValue = event.value)
			}

			SettingsEvent.UrlEditConfirmed -> viewModelScope.launch {
				val url = _state.value.editingUrlValue.trim()
				if (url.isNotBlank()) {
					saveCatalogUrl(url)
					_state.update { it.copy(showUrlEditDialog = false) }
					_effect.send(SettingsEffect.ShowSnackbar("URL saved"))
				}
			}

			SettingsEvent.UrlEditDismissed -> _state.update {
				it.copy(showUrlEditDialog = false)
			}

			SettingsEvent.UrlResetToDefault -> viewModelScope.launch {
				saveCatalogUrl(AppConstants.DEFAULT_CATALOG_URL)
				_state.update { it.copy(showUrlEditDialog = false) }
				_effect.send(SettingsEffect.ShowSnackbar("URL set to default"))
			}

			SettingsEvent.EditUsernameClicked -> _state.update {
				it.copy(showUsernameEditDialog = true, editingUsernameValue = it.username)
			}

			is SettingsEvent.UsernameEditChanged -> _state.update {
				it.copy(editingUsernameValue = event.value)
			}

			SettingsEvent.UsernameEditConfirmed -> viewModelScope.launch {
				val name = _state.value.editingUsernameValue.trim()
				if (name.isNotBlank()) {
					saveUsername(name)
					_state.update { it.copy(showUsernameEditDialog = false) }
					_effect.send(SettingsEffect.ShowSnackbar("Name saved"))
				}
			}

			SettingsEvent.UsernameEditDismissed -> _state.update {
				it.copy(showUsernameEditDialog = false)
			}

			SettingsEvent.ClearDataClicked -> _state.update {
				it.copy(showClearDataDialog = true)
			}

			SettingsEvent.ClearDataConfirmed -> viewModelScope.launch {
				_state.update { it.copy(isClearingData = true, showClearDataDialog = false) }
				runCatching { clearAllData() }
					.onSuccess {
						_state.update { it.copy(isClearingData = false) }
						_effect.send(SettingsEffect.ShowSnackbar("All data cleared"))
					}
					.onFailure { error ->
						_state.update { it.copy(isClearingData = false) }
						_effect.send(SettingsEffect.ShowSnackbar(error.message ?: "Error clearing data"))
					}
			}

			SettingsEvent.ClearDataDismissed -> _state.update {
				it.copy(showClearDataDialog = false)
			}

			SettingsEvent.ImportFromFileClicked -> viewModelScope.launch {
				_effect.send(SettingsEffect.ShowSnackbar("Coming soon"))
			}

			SettingsEvent.UpdateFromWebClicked -> viewModelScope.launch {
				_effect.send(SettingsEffect.ShowSnackbar("Coming soon"))
			}

			SettingsEvent.AboutInfoClicked -> _state.update {
				it.copy(showAboutInfoDialog = true)
			}

			SettingsEvent.AboutInfoDismissed -> _state.update {
				it.copy(showAboutInfoDialog = false)
			}
		}
	}
}
