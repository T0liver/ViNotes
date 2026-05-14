package hu.toliver.vinotes.ui.screen.winedetail

import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.model.Wine

data class WineDetailState(
    val isLoading: Boolean = true,
    val wine: Wine? = null,
    val tastings: List<Taste> = emptyList(),
    val radarData: RadarData? = null,
    val isEditSheetOpen: Boolean = false,
    val showDeleteWineDialog: Boolean = false,
    val errorMessage: String? = null,
)

data class RadarData(
    val acidity: Float,
    val tannin: Float,
    val body: Float,
    val alcohol: Float,
    val finish: Float,
)

