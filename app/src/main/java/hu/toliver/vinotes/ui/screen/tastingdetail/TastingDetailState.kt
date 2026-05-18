package hu.toliver.vinotes.ui.screen.tastingdetail

import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.model.Wine

data class TastingDetailState(
    val isLoading: Boolean = true,
    val taste: Taste? = null,
    val wine: Wine? = null,
    val showDeleteDialog: Boolean = false,
    val errorMessage: String? = null,
)
