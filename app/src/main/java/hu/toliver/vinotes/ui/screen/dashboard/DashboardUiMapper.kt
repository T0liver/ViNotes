package hu.toliver.vinotes.ui.screen.dashboard

import hu.toliver.vinotes.domain.model.TasteWithWine
import hu.toliver.vinotes.domain.model.enums.WineColour
import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormatter = SimpleDateFormat("yyyy. MMM d.", Locale.forLanguageTag("hu"))

fun TasteWithWine.toRecentItem(): RecentTastingItem = RecentTastingItem(
    tasteId = taste.id,
    wineId = wine.id,
    wineName = wine.name,
    producer = wine.producer,
    year = wine.year,
    rating = taste.rating,
    date = dateFormatter.format(taste.date),
    colourHex = wine.colour.toHex(),
)

private fun WineColour.toHex(): String = when (this) {
    WineColour.GRAY -> "#A8B8A0"
    WineColour.ORANGE -> "#C4682A"
    WineColour.WHITE -> "#C8A951"
    WineColour.YELLOW -> "#D4B866"
    WineColour.ROSE -> "#E8748A"
    WineColour.SHILLER -> "#D4B866"
    WineColour.TAWNY -> "#8B5A2B"
    WineColour.RED -> "#7B1D3A"
}
