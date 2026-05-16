package hu.toliver.vinotes.ui.screen.winedetail

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.toliver.vinotes.ui.screen.winedetail.components.WineDetailContent
import hu.toliver.vinotes.ui.screen.winedetail.components.WineDetailErrorContent
import hu.toliver.vinotes.ui.screen.winedetail.components.WineDetailLoadingContent
import hu.toliver.vinotes.ui.screen.winelist.components.DeleteConfirmDialog
import hu.toliver.vinotes.ui.screen.winelist.components.WineFormSheet
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WineDetailScreen(
    wineId: String,
    viewModel: WineDetailViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onAddTasting: (wineId: String) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(wineId) {
        viewModel.initializeWithWineId(wineId)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                WineDetailEffect.NavigateUp ->
                    onNavigateUp()
                is WineDetailEffect.NavigateToAddTasting ->
                    onAddTasting(effect.wineId)
                is WineDetailEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    if (state.showDeleteWineDialog) {
        DeleteConfirmDialog(
            wineName = state.wine?.name ?: "",
            onConfirm = { viewModel.onEvent(WineDetailEvent.DeleteWineConfirmed) },
            onDismiss = { viewModel.onEvent(WineDetailEvent.DeleteWineDismissed) },
        )
    }

    if (state.isEditSheetOpen && state.wine != null) {
        WineFormSheet(
            editingWine = state.wine,
            onSave = { wine, _ -> viewModel.onEvent(WineDetailEvent.WineSaved(wine)) },
            onDelete = { viewModel.onEvent(WineDetailEvent.DeleteWineClicked) },
            onDismiss = { viewModel.onEvent(WineDetailEvent.EditSheetDismissed) },
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.wine != null) {
                        IconButton(onClick = { viewModel.onEvent(WineDetailEvent.EditWineClicked) }) {
                            Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                ),
            )
        },
    ) { innerPadding ->
        when {
            state.isLoading -> WineDetailLoadingContent(Modifier.padding(innerPadding))
            state.wine == null -> WineDetailErrorContent(
                message = state.errorMessage ?: "The wine could not be found.",
                onBack = onNavigateUp,
                modifier = Modifier.padding(innerPadding),
            )
            else -> WineDetailContent(
                state = state,
                onEvent = viewModel::onEvent,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

