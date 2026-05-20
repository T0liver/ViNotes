package hu.toliver.vinotes.ui.screen.tastingdetail.components

import android.content.Context
import hu.toliver.vinotes.ui.screen.UIConverter.toDisplayName
import hu.toliver.vinotes.ui.screen.UIConverter.toFloat
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

fun Taste.toUiData(wine: Wine, context: Context): TastingDetailUiData {
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

        clarity = this.clarity.toDisplayName(context.resources),
        colourIntensity = this.colourIntensity.toDisplayName(context.resources),
        colour = this.colour.toDisplayName(context),
        otherVisual = this.otherVisual,

        noseIntensity = this.noseIntensity.toDisplayName(context.resources),
        noseAroma = this.noseAroma,
        noseDevelopment = this.noseDevelopment.toDisplayName(context.resources),
        otherNose = this.otherNose,

        sweetness = this.sweetness.toDisplayName(context.resources),
        acidity = this.acidity.toDisplayName(context.resources) to this.acidity.toFloat(),
        tannin = this.tannin.toDisplayName(context.resources) to this.tannin.toFloat(),
        body = this.body.toDisplayName(context.resources) to this.body.toFloat(),
        alcohol = this.alcohol.toDisplayName(context.resources) to this.alcohol.toFloat(),
        flavourIntensity = this.flavourIntensity.toDisplayName(context.resources),
        flavourCharacteristics = this.flavourCharacteristics,
        finish = this.finish.toDisplayName(context.resources) to this.finish.toFloat(),
        otherPalate = this.otherPalate,

        overallImpression = this.overallImpression,
        bestWith = this.bestWith,
        wouldDrinkAgain = this.wouldDrinkAgain,
    )
}

