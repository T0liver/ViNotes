package hu.toliver.vinotes.ui.screen.addtasting

import android.Manifest
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.toliver.vinotes.ui.screen.addtasting.components.TastingStepIndicator
import hu.toliver.vinotes.ui.screen.addtasting.components.TastingStepNavBar
import hu.toliver.vinotes.ui.screen.addtasting.steps.NoseStepContent
import hu.toliver.vinotes.ui.screen.addtasting.steps.PalateStepContent
import hu.toliver.vinotes.ui.screen.addtasting.steps.SummaryStepContent
import hu.toliver.vinotes.ui.screen.addtasting.steps.VisualStepContent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTastingScreen(
    preselectedWineId: String?,
    viewModel: AddTastingViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.onEvent(AddTastingEvent.OnLocationPermissionGranted)
        } else {
            viewModel.onEvent(AddTastingEvent.OnLocationPermissionDenied)
        }
    }

    LaunchedEffect(preselectedWineId) {
        viewModel.onEvent(
            AddTastingEvent.Init(
                wineId = preselectedWineId ?: "",
                editTasteId = null,
                // TODO: On edit Screen.AddTasting needs to extended more
            )
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AddTastingEffect.NavigateUp -> onNavigateUp()
                is AddTastingEffect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
                AddTastingEffect.RequestLocationPermission -> locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                    )
                )
                is AddTastingEffect.ShowLocationError -> Unit
            }
        }
    }

    LaunchedEffect(state.locationError) {
        state.locationError?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.isEditMode) "Edit tasting"
                        else "New tasting",
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TastingStepIndicator(
                stepCount = viewModel.stepCount,
                currentStep = state.currentStep,
                stepLabels = listOf("Visual", "Nose", "Taste", "Rating"),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            AnimatedContent(
                targetState = state.currentStep,
                modifier = Modifier.weight(1f),
                transitionSpec = {
                    val forward = targetState > initialState
                    (slideInHorizontally { if (forward) it else -it } + fadeIn())
                        .togetherWith(slideOutHorizontally { if (forward) -it else it } + fadeOut())
                },
                label = "step_transition",
            ) { step ->
                when (step) {
                    0 -> VisualStepContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.fillMaxSize(),
                    )

                    1 -> NoseStepContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.fillMaxSize(),
                    )

                    2 -> PalateStepContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.fillMaxSize(),
                    )

                    3 -> SummaryStepContent(
                        state = state,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            TastingStepNavBar(
                currentStep = state.currentStep,
                stepCount = viewModel.stepCount,
                isSaving = state.isSaving,
                onPrev = { viewModel.onEvent(AddTastingEvent.PrevStep) },
                onNext = { viewModel.onEvent(AddTastingEvent.NextStep) },
                onSave = { viewModel.onEvent(AddTastingEvent.SaveTasting) },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }
    }
}
