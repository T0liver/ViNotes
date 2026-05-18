package hu.toliver.vinotes.ui.screen.tastingdetail.components

import hu.toliver.vinotes.data.local.converters.UIConverter.toDisplayName
import hu.toliver.vinotes.data.local.converters.UIConverter.toFloat
import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.model.Wine
import java.text.SimpleDateFormat
import java.util.Locale

data class TastingDetailUiData(
    val tasteId: String,
    val wineName: String,
    val producer: String,
    val year: Int,
    val vintage: String,
    val rating: Int,
    val date: String,
    val place: String,

    // Visual
    val clarity: String,
    val colourIntensity: String,
    val colour: String,
    val otherVisual: String,

    // Nose
    val noseIntensity: String,
    val noseAroma: String,
    val noseDevelopment: String,
    val otherNose: String,

    // Palate
    val sweetness: String,
    val acidity: Pair<String, Float>,
    val tannin: Pair<String, Float>,
    val body: Pair<String, Float>,
    val alcohol: Pair<String, Float>,
    val flavourIntensity: String,
    val flavourCharacteristics: String,
    val finish: Pair<String, Float>,
    val otherPalate: String,

    // Overall
    val overallImpression: String,
    val bestWith: String,
    val wouldDrinkAgain: Boolean,
)

fun Taste.toUiData(wine: Wine): TastingDetailUiData {
    val year = wine.year
    return TastingDetailUiData(
        tasteId = this.id,
        wineName = wine.name,
        producer = wine.producer,
        year = year,
        vintage = if (year > 0) year.toString() else wine.year.toString(),
        rating = this.rating,
        date = SimpleDateFormat("yyyy. MMM d. HH:mm", Locale.getDefault()).format(this.date),
        place = this.place,

        clarity = this.clarity.toDisplayName(),
        colourIntensity = this.colourIntensity.toDisplayName(),
        colour = this.colour.name,
        otherVisual = this.otherVisual,

        noseIntensity = this.noseIntensity.toDisplayName(),
        noseAroma = this.noseAroma,
        noseDevelopment = this.noseDevelopment.toDisplayName(),
        otherNose = this.otherNose,

        sweetness = this.sweetness.toDisplayName(),
        acidity = this.acidity.toDisplayName() to this.acidity.toFloat(),
        tannin = this.tannin.toDisplayName() to this.tannin.toFloat(),
        body = this.body.toDisplayName() to this.body.toFloat(),
        alcohol = this.alcohol.toDisplayName() to this.alcohol.toFloat(),
        flavourIntensity = this.flavourIntensity.toDisplayName(),
        flavourCharacteristics = this.flavourCharacteristics,
        finish = this.finish.toDisplayName() to this.finish.toFloat(),
        otherPalate = this.otherPalate,

        overallImpression = this.overallImpression,
        bestWith = this.bestWith,
        wouldDrinkAgain = this.wouldDrinkAgain,
    )
}

