package hu.toliver.vinotes.ui.screen.addtasting

import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.domain.model.enums.TasteWineColour
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import java.util.Date

data class AddTastingState(

    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val isLoadingLocation: Boolean = false,
    val isEditMode: Boolean = false,
    val editingTasteId: String? = null,
    val wineId: String = "",
    val currentStep: Int = 0,
    val errorMessage: String? = null,
    val locationError: String? = null,

    // Visuals
    val clarity: WineClarity = WineClarity.CLEAR,
    val colourIntensity: WineColourIntensity = WineColourIntensity.PALE,
    val colour: TasteWineColour = TasteWineColour.WHITE,
    val otherVisual: String = "",

    // Nose
    val noseIntensity: Intensity = Intensity.NONE,
    val noseAroma: String = "",
    val noseDevelopment: NoseDevelopment = NoseDevelopment.YOUTHFUL,
    val otherNose: String = "",

    // Taste/Palate
    val sweetness: WineSweetness = WineSweetness.BRUT_NATURE,
    val acidity: Level = Level.MEDIUM,
    val tannin: Level = Level.MEDIUM,
    val body: Level = Level.MEDIUM,
    val alcohol: Level = Level.MEDIUM,
    val flavourIntensity: Intensity = Intensity.NONE,
    val flavourCharacteristics: String = "",
    val finish: Level = Level.MEDIUM,
    val otherPalate: String = "",

    // Summary
    val rating: Int = 80,
    val overallImpression: String = "",
    val bestWith: String = "",
    val wouldDrinkAgain: Boolean = true,
    val date: Date = Date(),
    val place: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
)
