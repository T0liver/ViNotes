package hu.toliver.vinotes.ui.screen.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.model.enums.ThemeMode
import hu.toliver.vinotes.ui.screen.settings.components.AboutInfoDialog
import hu.toliver.vinotes.ui.screen.settings.components.AboutSection
import hu.toliver.vinotes.ui.screen.settings.components.ClearDataConfirmDialog
import hu.toliver.vinotes.ui.screen.settings.components.SettingsActionItem
import hu.toliver.vinotes.ui.screen.settings.components.SettingsSectionHeader
import hu.toliver.vinotes.ui.screen.settings.components.UrlEditDialog
import hu.toliver.vinotes.ui.screen.settings.components.UsernameEditDialog
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current
    val importFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri ->
        uri?.let { viewModel.onEvent(SettingsEvent.ImportFileSelected(it)) }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SettingsEffect.NavigateUp -> onNavigateUp()
                is SettingsEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                is SettingsEffect.OpenUrl -> uriHandler.openUri(effect.url)
                SettingsEffect.OpenImportFilePicker -> importFileLauncher.launch(
                    arrayOf(
                        "application/json",
                        "text/json",
                        "text/plain",
                    )
                )
            }
        }
    }

    if (state.showClearDataDialog) {
        ClearDataConfirmDialog(
            onConfirm = { viewModel.onEvent(SettingsEvent.ClearDataConfirmed) },
            onDismiss = { viewModel.onEvent(SettingsEvent.ClearDataDismissed) },
        )
    }

    if (state.showUrlEditDialog) {
        UrlEditDialog(
            value = state.editingUrlValue,
            onChange = { viewModel.onEvent(SettingsEvent.UrlEditChanged(it)) },
            onConfirm = { viewModel.onEvent(SettingsEvent.UrlEditConfirmed) },
            onReset = { viewModel.onEvent(SettingsEvent.UrlResetToDefault) },
            onDismiss = { viewModel.onEvent(SettingsEvent.UrlEditDismissed) },
        )
    }

    if (state.showUsernameEditDialog) {
        UsernameEditDialog(
            value = state.editingUsernameValue,
            onChange = { viewModel.onEvent(SettingsEvent.UsernameEditChanged(it)) },
            onConfirm = { viewModel.onEvent(SettingsEvent.UsernameEditConfirmed) },
            onDismiss = { viewModel.onEvent(SettingsEvent.UsernameEditDismissed) },
        )
    }

    if (state.showAboutInfoDialog) {
        AboutInfoDialog(
            onDismiss = { viewModel.onEvent(SettingsEvent.AboutInfoDismissed) },
        )
    }

    if (state.showThemeDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.ThemeDialogDismissed) },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ThemeDialogDismissed) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = { Text(stringResource(R.string.settings)) },
            text = {
                Column {
                    val current = state.themeMode
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = current == ThemeMode.LIGHT, onClick = { viewModel.onEvent(SettingsEvent.ThemeSelected(ThemeMode.LIGHT)) })
                        Text(text = stringResource(R.string.light))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = current == ThemeMode.DARK, onClick = { viewModel.onEvent(SettingsEvent.ThemeSelected(ThemeMode.DARK)) })
                        Text(text = stringResource(R.string.dark))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = current == ThemeMode.SYSTEM, onClick = { viewModel.onEvent(SettingsEvent.ThemeSelected(ThemeMode.SYSTEM)) })
                        Text(text = stringResource(R.string.system_settings))
                    }
                }
            }
        )
    }

    if (state.showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.LanguageDialogDismissed) },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.LanguageDialogDismissed) }) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = { Text(stringResource(R.string.app_language)) },
            text = {
                Column {
                    val current = state.appLanguage
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = current == AppLanguage.SYSTEM, onClick = { viewModel.onEvent(SettingsEvent.LanguageSelected(AppLanguage.SYSTEM)) })
                        Text(text = stringResource(R.string.system_settings))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = current == AppLanguage.ENGLISH, onClick = { viewModel.onEvent(SettingsEvent.LanguageSelected(AppLanguage.ENGLISH)) })
                        Text(text = stringResource(R.string.english))
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = current == AppLanguage.HUNGARIAN, onClick = { viewModel.onEvent(SettingsEvent.LanguageSelected(AppLanguage.HUNGARIAN)) })
                        Text(text = stringResource(R.string.hungarian))
                    }
                }
            },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), style = typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.Edit,
                    title = stringResource(R.string.set_username),
                    subtitle = state.username.ifBlank { stringResource(R.string.not_set) },
                    onClick = { viewModel.onEvent(SettingsEvent.EditUsernameClicked) },
                )
            }

            item {
                SettingsActionItem(
                    icon = Icons.Outlined.ModeNight,
                    title = stringResource(R.string.theme),
                    subtitle = when (state.themeMode) {
                        ThemeMode.LIGHT -> stringResource(R.string.light_colour)
                        ThemeMode.DARK -> stringResource(R.string.dark)
                        ThemeMode.SYSTEM -> stringResource(R.string.system_settings)
                    },
                    onClick = { viewModel.onEvent(SettingsEvent.ThemeClicked) },
                )
            }
/*
This will be removed for release 1.0, I can't manage to work the change work,
    Google should fix the damn AppCompatDelegate.setApplicationLocales already,
    it's been months, and it still does not work, even though it returns the correct
    locale list and logs show that the app language is changed,
    the locale just does not change at all.
    So for now, I'm hiding the option to change app language in the settings,
    it will be only changeable through system settings, which works just fine.
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.Language,
                    title = stringResource(R.string.app_language),
                    subtitle = when (state.appLanguage) {
                        AppLanguage.SYSTEM -> stringResource(R.string.system_settings)
                        AppLanguage.ENGLISH -> stringResource(R.string.english)
                        AppLanguage.HUNGARIAN -> stringResource(R.string.hungarian)
                    },
                    onClick = { viewModel.onEvent(SettingsEvent.LanguageClicked) },
                )
            }
*/
            item { SettingsSectionHeader(stringResource(R.string.synchronization)) }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.FileUpload,
                    title = stringResource(R.string.import_wines_from_file),
                    subtitle = stringResource(R.string.load_json_file_from_device),
                    onClick = { viewModel.onEvent(SettingsEvent.ImportFromFileClicked) },
                    isLoading = state.isImportingCatalog,
                )
            }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.CloudSync,
                    title = stringResource(R.string.update_catalog_from_web),
                    subtitle = stringResource(R.string.download_catalog, state.catalogUrl),
                    onClick = { viewModel.onEvent(SettingsEvent.UpdateFromWebClicked) },
                    trailingContent = {
                        IconButton(
                            onClick = { viewModel.onEvent(SettingsEvent.EditUrlClicked) },
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = stringResource(R.string.edit_url),
                                tint = colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                )
            }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.CloudSync,
                    title = stringResource(R.string.update_catalog_delta),
                    subtitle = stringResource(R.string.sync_delta_changes),
                    onClick = { viewModel.onEvent(SettingsEvent.UpdateDeltaSyncClicked) },
                    isLoading = state.isSyncingCatalog,
                )
            }

            item { SettingsSectionHeader(stringResource(R.string.datas)) }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.DeleteForever,
                    iconTint = colorScheme.error,
                    title = stringResource(R.string.delete_all_data),
                    subtitle = stringResource(R.string.all_wine_and_tastings_get_deleted),
                    isDestructive = true,
                    isLoading = state.isClearingData,
                    onClick = { viewModel.onEvent(SettingsEvent.ClearDataClicked) },
                )
            }

            item { SettingsSectionHeader(stringResource(R.string.about)) }
            item {
                AboutSection(
                    onInfoClick = { viewModel.onEvent(SettingsEvent.AboutInfoClicked) },
                )
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}