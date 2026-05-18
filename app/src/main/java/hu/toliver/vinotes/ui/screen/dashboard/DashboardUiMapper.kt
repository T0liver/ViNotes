package hu.toliver.vinotes.ui.screen.dashboard

import hu.toliver.vinotes.data.local.converters.UIConverter.fromColourToHex
import hu.toliver.vinotes.domain.model.TasteWithWine
import java.text.SimpleDateFormat
import java.util.Locale

private val dateFormatter = SimpleDateFormat("yyyy. MMM d.", Locale.forLanguageTag("hu"))

fun TasteWithWine.toRecentItem(): RecentTastingItem = RecentTastingItem(
    tasteId = taste.id,
    wineId = wine.id,
    wineName = wine.name,
    producer = wine.producer,
    year = wine.year,
    place = taste.place,
    rating = taste.rating,
    date = dateFormatter.format(taste.date),
    colourHex = wine.colour.fromColourToHex(),
)
