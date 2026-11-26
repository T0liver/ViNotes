package hu.toliver.vinotes.ui.screen.winelist.components

import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.ui.screen.winelist.WineFilters

fun isWineFormDirty(
    editingWine: Wine?,
    name: String,
    producer: String,
    year: String,
    grape: String,
    isCuvee: Boolean,
    cuveeText: String,
    selectedColour: WineColour,
    country: String,
    region: String,
    alcoholText: String,
    description: String,
): Boolean {
    if (editingWine == null) {
        return name.isNotBlank()
            || producer.isNotBlank()
            || year.isNotBlank()
            || grape.isNotBlank()
            || isCuvee
            || cuveeText.isNotBlank()
            || selectedColour != WineColour.RED
            || country.isNotBlank()
            || region.isNotBlank()
            || alcoholText.isNotBlank()
            || description.isNotBlank()
    }
    val wine = editingWine
    return name != wine.name
        || producer != wine.producer
        || year != wine.year.toString()
        || grape != wine.grape
        || isCuvee != wine.isCuvee
        || cuveeText != wine.cuveeComponents.joinToString(", ")
        || selectedColour != wine.colour
        || country != wine.country
        || region != wine.region
        || alcoholText != (wine.alcoholPercentage.takeIf { it != 0.0 }?.toString() ?: "")
        || description != wine.description
}

fun isWineFilterDirty(
    currentFilters: WineFilters,
    selectedColours: List<WineColour>,
    selectedCountries: List<String>,
    yearFromText: String,
    yearToText: String,
): Boolean {
    return selectedColours.toSet() != currentFilters.colours
        || selectedCountries.toSet() != currentFilters.countries
        || yearFromText.toIntOrNull() != currentFilters.yearFrom
        || yearToText.toIntOrNull() != currentFilters.yearTo
}