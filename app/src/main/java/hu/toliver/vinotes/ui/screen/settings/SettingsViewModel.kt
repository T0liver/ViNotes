package hu.toliver.vinotes.ui.screen.settings

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.usecases.catalog.ImportFromFileUseCase
import hu.toliver.vinotes.domain.usecases.catalog.PerformDeltaSyncUseCase
import hu.toliver.vinotes.domain.usecases.catalog.PerformFullImportUseCase
import hu.toliver.vinotes.domain.usecases.settings.ClearAllDataUseCase
import hu.toliver.vinotes.domain.usecases.settings.GetAppPreferencesUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveAppLanguageUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveCatalogUrlUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveThemeModeUseCase
import hu.toliver.vinotes.domain.usecases.settings.SaveUsernameUseCase
import hu.toliver.vinotes.ui.AppConstants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	@param:ApplicationContext private val context: Context,
	private val getAppPreferences: GetAppPreferencesUseCase,
	private val saveCatalogUrl: SaveCatalogUrlUseCase,
	private val saveUsername: SaveUsernameUseCase,
	private val saveThemeMode: SaveThemeModeUseCase,
	private val saveAppLanguage: SaveAppLanguageUseCase,
	private val clearAllData: ClearAllDataUseCase,
	private val importFromFile: ImportFromFileUseCase,
	private val performFullImport: PerformFullImportUseCase,
	private val performDeltaSync: PerformDeltaSyncUseCase,
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
							themeMode = prefs.themeMode,
							appLanguage = prefs.appLanguage,
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
					_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.url_saved)))
				}
			}

			SettingsEvent.UrlEditDismissed -> _state.update {
				it.copy(showUrlEditDialog = false)
			}

			SettingsEvent.UrlResetToDefault -> viewModelScope.launch {
				saveCatalogUrl(AppConstants.DEFAULT_CATALOG_URL)
				_state.update { it.copy(showUrlEditDialog = false) }
				_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.url_set_to_default)))
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
					_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.name_saved)))
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
						_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.all_data_cleared)))
					}
					.onFailure { error ->
						_state.update { it.copy(isClearingData = false) }
						_effect.send(SettingsEffect.ShowSnackbar(error.message ?: context.getString(
							R.string.error_clearing_data
						)))
					}
			}

		SettingsEvent.ClearDataDismissed -> _state.update {
			it.copy(showClearDataDialog = false)
		}

			SettingsEvent.ImportFromFileClicked -> viewModelScope.launch {
				_effect.send(SettingsEffect.OpenImportFilePicker)
			}

			is SettingsEvent.ImportFileSelected -> viewModelScope.launch {
				_state.update { it.copy(isImportingCatalog = true) }
				importFromFile(event.uri)
					.onSuccess {
						_state.update { it.copy(isImportingCatalog = false) }
						_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.catalog_imported_from_file)))
					}
					.onFailure { error ->
						_state.update { it.copy(isImportingCatalog = false) }
						_effect.send(
							SettingsEffect.ShowSnackbar(
								error.message ?: context.getString(R.string.error_importing_catalog_from_file)
							)
						)
					}
		}

		SettingsEvent.UpdateFromWebClicked -> viewModelScope.launch {
			_state.update { it.copy(isSyncingCatalog = true) }
			performFullImport()
				.onSuccess {
					_state.update { it.copy(isSyncingCatalog = false) }
					_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.full_catalog_imported)))
				}
				.onFailure { error ->
					_state.update { it.copy(isSyncingCatalog = false) }
					val msg = error.message ?: context.getString(R.string.error_syncing_catalog)
					// Detect DNS resolution failures and show actionable hint
					if (msg.contains(context.getString(R.string.dns_resolution_failed), ignoreCase = true)
						|| msg.contains(context.getString(R.string.unable_to_resolve_host), ignoreCase = true)
						|| msg.contains(context.getString(R.string.no_address_associated_with_hostname), ignoreCase = true)) {
						_effect.send(
							SettingsEffect.ShowSnackbar(
								context.getString(R.string.cannot_resolve_catalog_host)
							)
						)
					} else {
						_effect.send(SettingsEffect.ShowSnackbar(msg))
					}
				}
		}

		SettingsEvent.UpdateDeltaSyncClicked -> viewModelScope.launch {
			_state.update { it.copy(isSyncingCatalog = true) }
			performDeltaSync()
				.onSuccess {
					_state.update { it.copy(isSyncingCatalog = false) }
					_effect.send(SettingsEffect.ShowSnackbar(context.getString(R.string.catalog_updated)))
				}
				.onFailure { error ->
					_state.update { it.copy(isSyncingCatalog = false) }
					val msg = error.message ?: context.getString(R.string.error_syncing_catalog)
					// Detect DNS resolution failures and show actionable hint
					if (msg.contains(context.getString(R.string.dns_resolution_failed), ignoreCase = true)
						|| msg.contains(context.getString(R.string.unable_to_resolve_host), ignoreCase = true)
						|| msg.contains(context.getString(R.string.no_address_associated_with_hostname), ignoreCase = true)) {
						_effect.send(
							SettingsEffect.ShowSnackbar(
								context.getString(R.string.cannot_resolve_catalog_host)
							)
						)
					} else {
						_effect.send(SettingsEffect.ShowSnackbar(msg))
					}
				}
		}

			SettingsEvent.AboutInfoClicked -> _state.update {
				it.copy(showAboutInfoDialog = true)
			}

			SettingsEvent.AboutInfoDismissed -> _state.update {
				it.copy(showAboutInfoDialog = false)
			}

			SettingsEvent.ThemeClicked -> _state.update {
				it.copy(showThemeDialog = true)
			}

			is SettingsEvent.ThemeSelected -> viewModelScope.launch {
				saveThemeMode(event.theme)
				_state.update { it.copy(showThemeDialog = false, themeMode = event.theme) }
			}

			SettingsEvent.ThemeDialogDismissed -> _state.update {
				it.copy(showThemeDialog = false)
			}

			SettingsEvent.LanguageClicked -> _state.update {
				it.copy(showLanguageDialog = true)
			}

			is SettingsEvent.LanguageSelected -> viewModelScope.launch {
				saveAppLanguage(event.language)
				_state.update { it.copy(showLanguageDialog = false, appLanguage = event.language) }
				val appLocale = LocaleListCompat.forLanguageTags(
					when (event.language) {
						AppLanguage.SYSTEM -> ""
						AppLanguage.ENGLISH -> "en"
						AppLanguage.HUNGARIAN -> "hu"
					}
				)
				Log.d("LOCALE", "App language changed to ${event.language}, setting app locales to $appLocale")
				AppCompatDelegate.setApplicationLocales(appLocale)
				// this here does not set the locale at all.
				Log.d("LOCALE", "Current application locales: ${AppCompatDelegate.getApplicationLocales()}")
			}

			SettingsEvent.LanguageDialogDismissed -> _state.update {
				it.copy(showLanguageDialog = false)
			}
		}
	}
}