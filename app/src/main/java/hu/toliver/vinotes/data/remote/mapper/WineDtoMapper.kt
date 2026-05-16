package hu.toliver.vinotes.data.remote.mapper

import hu.toliver.vinotes.data.remote.dto.WineDto
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness

fun String.toWineColour(): WineColour =
    runCatching { WineColour.valueOf(this) }.getOrDefault(WineColour.RED)

fun String.toWineSweetness(): WineSweetness =
    runCatching { WineSweetness.valueOf(this) }.getOrDefault(WineSweetness.DRY)

fun WineDto.toDomain(): Wine = Wine(
    id = id,
    name = name,
    producer = producer,
    year = year,
    grape = grape,
    isCuvee = isCuvee,
    cuveeComponents = cuveeComponents,
    colour = colour.toWineColour(),
    sugar = sugar,
    sweetness = sweetness.toWineSweetness(),
    country = country,
    region = region,
    alcoholPercentage = alcoholPercentage,
    volume = volume,
    description = description,
    image = image
)