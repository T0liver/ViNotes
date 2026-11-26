package hu.toliver.vinotes.data.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import hu.toliver.vinotes.data.WineDatabase
import hu.toliver.vinotes.data.local.entity.TasteEntity
import hu.toliver.vinotes.data.local.entity.WineEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CatalogImportPreservesDataTest {

    private lateinit var database: WineDatabase
    private lateinit var wineDao: WineDao
    private lateinit var tasteDao: TasteDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, WineDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        wineDao = database.wineDao()
        tasteDao = database.tasteDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun catalogReimportPreservesTastingsAndUserEdits() = runBlocking {
        val wineId = "catalog-wine-1"
        val original = sampleCatalogWine(id = wineId, producer = "Original Producer", year = 2020)
        wineDao.insertAll(listOf(original))

        val tasting = sampleTasting(wineId = wineId, id = "taste-1")
        tasteDao.insert(tasting)

        val userEdited = original.copy(producer = "User Producer", year = 2019)
        wineDao.update(userEdited)

        val reimportFromCatalog = sampleCatalogWine(
            id = wineId,
            producer = "Catalog Producer",
            year = 2022,
            isFromCatalog = true
        )
        wineDao.insertAll(listOf(reimportFromCatalog))

        val storedWine = wineDao.getById(wineId)
        assertNotNull(storedWine)
        assertEquals("User Producer", storedWine!!.producer)
        assertEquals(2019, storedWine.year)

        val tastings = tasteDao.getByWineId(wineId).first()
        assertEquals(1, tastings.size)
        assertEquals("taste-1", tastings.first().id)
    }

    private fun sampleCatalogWine(
        id: String,
        producer: String,
        year: Int,
        isFromCatalog: Boolean = true
    ) = WineEntity(
        id = id,
        name = "Test Wine",
        producer = producer,
        year = year,
        grape = "Test Grape",
        isCuvee = false,
        cuveeComponents = "",
        country = "HU",
        region = "Test Region",
        colour = "WHITE",
        sweetness = "DRY",
        sugar = 0f,
        alcoholPercentage = 12.0,
        volume = 750,
        description = "",
        imageUrl = "",
        isFromCatalog = isFromCatalog
    )

    private fun sampleTasting(wineId: String, id: String) = TasteEntity(
        id = id,
        wineId = wineId,
        clarity = "CLEAR",
        colourIntensity = "MEDIUM",
        colour = "WHITE",
        otherVisual = "",
        noseIntensity = "MEDIUM",
        noseAroma = "",
        noseDevelopment = "DEVELOPED",
        otherNose = "",
        sweetness = "DRY",
        acidity = "MEDIUM",
        tannin = "LOW",
        body = "MEDIUM",
        alcohol = "MEDIUM",
        flavourIntensity = "MEDIUM",
        flavourCharacteristics = "",
        finish = "MEDIUM",
        otherPalate = "",
        rating = 4,
        overallImpression = "",
        bestWith = "",
        wouldDrinkAgain = true,
        date = 1_700_000_000_000L,
        place = "Home",
        latitude = null,
        longitude = null
    )
}