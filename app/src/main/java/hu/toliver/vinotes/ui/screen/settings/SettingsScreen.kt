package hu.toliver.vinotes.ui.screen.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CloudSync
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SettingsEffect.NavigateUp -> onNavigateUp()
                is SettingsEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                is SettingsEffect.OpenUrl -> uriHandler.openUri(effect.url)
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

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            item { SettingsSectionHeader("Datas") }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.DeleteForever,
                    iconTint = colorScheme.error,
                    title = "Delete all data",
                    subtitle = "All wine and tastings get deleted.",
                    isDestructive = true,
                    isLoading = state.isClearingData,
                    onClick = { viewModel.onEvent(SettingsEvent.ClearDataClicked) },
                )
            }

            item { SettingsSectionHeader("Synchronization") }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.FileUpload,
                    title = "Import wines from file",
                    subtitle = "Load JSON file from device",
                    onClick = { viewModel.onEvent(SettingsEvent.ImportFromFileClicked) },
                )
            }
            item {
                SettingsActionItem(
                    icon = Icons.Outlined.CloudSync,
                    title = "Update catalog from web",
                    subtitle = "Download catalog: ${state.catalogUrl}",
                    onClick = { viewModel.onEvent(SettingsEvent.UpdateFromWebClicked) },
                    trailingContent = {
                        IconButton(
                            onClick = { viewModel.onEvent(SettingsEvent.EditUrlClicked) },
                            modifier = Modifier.padding(start = 8.dp),
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Edit URL",
                                tint = colorScheme.onSurfaceVariant,
                            )
                        }
                    },
                )
            }

            item {
                SettingsActionItem(
                    icon = Icons.Outlined.Edit,
                    title = "Set username",
                    subtitle = state.username.ifBlank { "Not set" },
                    onClick = { viewModel.onEvent(SettingsEvent.EditUsernameClicked) },
                )
            }

            item { SettingsSectionHeader("About") }
            item {
                AboutSection(
                    onInfoClick = { viewModel.onEvent(SettingsEvent.AboutInfoClicked) },
                )
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }
}