package hu.toliver.vinotes.data.remote.mapper

import hu.toliver.vinotes.domain.model.enums.WineColour
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class WineColourParsingTest {

    @Test
    fun parseWineColour_acceptsRoséAccent() {
        assertEquals(WineColour.ROSE, parseWineColour("ROSÉ"))
    }

    @Test
    fun parseWineColour_acceptsRosePlainCaseInsensitive() {
        assertEquals(WineColour.ROSE, parseWineColour("rose"))
        assertEquals(WineColour.ROSE, parseWineColour("ROSE"))
    }

    @Test
    fun parseWineColour_rejectsUnknown() {
        assertNull(parseWineColour("BURGUNDY"))
    }

    @Test
    fun toWineColour_mapsOfficialCatalogueValues() {
        assertEquals(WineColour.RED, "RED".toWineColour())
        assertEquals(WineColour.ROSE, "ROSÉ".toWineColour())
    }
}