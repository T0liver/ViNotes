package hu.toliver.vinotes.ui.screen.winelist.components

import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import hu.toliver.vinotes.ui.screen.winelist.WineFilters
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SheetFormDirtyTest {

  @Test
    fun newWineForm_emptyIsNotDirty() {
        assertFalse(
            isWineFormDirty(
                editingWine = null,
                name = "",
                producer = "",
                year = "",
                grape = "",
                isCuvee = false,
                cuveeText = "",
                selectedColour = WineColour.RED,
                country = "",
                region = "",
                alcoholText = "",
                description = "",
            ),
        )
    }

    @Test
    fun newWineForm_anyInputIsDirty() {
        assertTrue(
            isWineFormDirty(
                editingWine = null,
                name = "Tokaji",
                producer = "",
                year = "",
                grape = "",
                isCuvee = false,
                cuveeText = "",
                selectedColour = WineColour.RED,
                country = "",
                region = "",
                alcoholText = "",
                description = "",
            ),
        )
    }

    @Test
    fun editWineForm_unchangedIsNotDirty() {
        val wine = sampleWine()
        assertFalse(
            isWineFormDirty(
                editingWine = wine,
                name = wine.name,
                producer = wine.producer,
                year = wine.year.toString(),
                grape = wine.grape,
                isCuvee = wine.isCuvee,
                cuveeText = wine.cuveeComponents.joinToString(", "),
                selectedColour = wine.colour,
                country = wine.country,
                region = wine.region,
                alcoholText = wine.alcoholPercentage.toString(),
                description = wine.description,
            ),
        )
    }

    @Test
    fun filterSheet_matchesAppliedFiltersIsNotDirty() {
        val filters = WineFilters(
            colours = setOf(WineColour.RED),
            countries = setOf("Hungary"),
            yearFrom = 2018,
            yearTo = 2022,
        )
        assertFalse(
            isWineFilterDirty(
                currentFilters = filters,
                selectedColours = listOf(WineColour.RED),
                selectedCountries = listOf("Hungary"),
                yearFromText = "2018",
                yearToText = "2022",
            ),
        )
    }

    @Test
    fun filterSheet_changedSelectionIsDirty() {
        assertTrue(
            isWineFilterDirty(
                currentFilters = WineFilters(),
                selectedColours = listOf(WineColour.WHITE),
                selectedCountries = emptyList(),
                yearFromText = "",
                yearToText = "",
            ),
        )
    }

    private fun sampleWine() = Wine(
        id = "id-1",
        name = "Sample",
        producer = "Producer",
        year = 2020,
        grape = "Pinot",
        isCuvee = false,
        cuveeComponents = emptyList(),
        colour = WineColour.RED,
        sugar = 0f,
        sweetness = WineSweetness.DRY,
        country = "Hungary",
        region = "Tokaj",
        alcoholPercentage = 12.5,
        volume = 750,
        description = "Notes",
        image = "",
    )
}