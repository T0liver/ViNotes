package hu.toliver.vinotes.ui.screen.addtasting

import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.domain.model.enums.TasteWineColour
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import java.util.Date

sealed interface AddTastingEvent {

    // Lifecycle
    data class Init(val wineId: String, val editTasteId: String? = null) : AddTastingEvent

    // Navigation
    data object NextStep : AddTastingEvent
    data object PrevStep : AddTastingEvent

    data class ClarityChanged(val v: WineClarity) : AddTastingEvent
    data class ColourIntensityChanged(val v: WineColourIntensity) : AddTastingEvent
    data class ColourChanged(val v: TasteWineColour) : AddTastingEvent
    data class OtherVisualChanged(val v: String) : AddTastingEvent

    data class NoseIntensityChanged(val v: Intensity) : AddTastingEvent
    data class NoseAromaChanged(val v: String) : AddTastingEvent
    data class NoseDevelopmentChanged(val v: NoseDevelopment) : AddTastingEvent
    data class OtherNoseChanged(val v: String) : AddTastingEvent

    data class SweetnessChanged(val v: WineSweetness) : AddTastingEvent
    data class AcidityChanged(val v: Level) : AddTastingEvent
    data class TanninChanged(val v: Level) : AddTastingEvent
    data class BodyChanged(val v: Level) : AddTastingEvent
    data class AlcoholChanged(val v: Level) : AddTastingEvent
    data class FlavourIntensityChanged(val v: Intensity) : AddTastingEvent
    data class FlavourCharacteristicsChanged(val v: String) : AddTastingEvent
    data class FinishChanged(val v: Level) : AddTastingEvent
    data class OtherPalateChanged(val v: String) : AddTastingEvent

    data class RatingChanged(val v: Int) : AddTastingEvent
    data class OverallImpressionChanged(val v: String) : AddTastingEvent
    data class BestWithChanged(val v: String) : AddTastingEvent
    data class WouldDrinkAgainChanged(val v: Boolean) : AddTastingEvent
    data class DateChanged(val v: Date) : AddTastingEvent
    data class PlaceChanged(val v: String) : AddTastingEvent

    data object SaveTasting : AddTastingEvent
}
