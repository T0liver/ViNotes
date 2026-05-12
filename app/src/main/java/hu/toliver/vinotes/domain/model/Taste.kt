package hu.toliver.vinotes.domain.model

import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.domain.model.enums.TasteWineColour
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import java.util.Date

/*
 * Guide to wine tasting parameters:
 * [WSET Level 3 Systematic Approach to Tasting Wine]
 * (https://www.wsetglobal.com/media/3119/wset_l3_wines_sat_en_jun-2016.pdf)
 */

class Taste (
    val id: String,
    // Visual
    val clarity: WineClarity,
    val colourIntensity: WineColourIntensity,
    val colour: TasteWineColour,
    val otherVisual: String,

    // Nose
    val noseIntensity: Intensity,
    val noseAroma: String,
    val noseDevelopment: NoseDevelopment,
    val otherNose: String,

    // Palate
    val sweetness: WineSweetness,
    val acidity: Level,
    val tannin: Level,
    val body: Level,
    val alcohol: Level,
    val flavourIntensity: Intensity,
    val flavourCharacteristics: String,
    val finish: Level,
    val otherPalate: String,

    // Overall rating (subjective)
    val rating: Int,
    val overallImpression: String,
    val bestWith: String,

    // Metadata
    val date: Date,
    val wineId: String,
    val place: String,
    val wouldDrinkAgain: Boolean
)
