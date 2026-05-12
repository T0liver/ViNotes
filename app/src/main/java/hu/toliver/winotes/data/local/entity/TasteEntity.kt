package hu.toliver.winotes.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.toliver.winotes.domain.model.Taste
import hu.toliver.winotes.domain.model.enums.Intensity
import hu.toliver.winotes.domain.model.enums.Level
import hu.toliver.winotes.domain.model.enums.NoseDevelopment
import hu.toliver.winotes.domain.model.enums.TasteWineColour
import hu.toliver.winotes.domain.model.enums.WineClarity
import hu.toliver.winotes.domain.model.enums.WineColourIntensity
import hu.toliver.winotes.domain.model.enums.WineSweetness
import java.util.Date

@Entity(
    tableName = "tastings",
    foreignKeys = [ForeignKey(
        entity = WineEntity::class,
        parentColumns = ["id"],
        childColumns = ["wineId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("wineId")]
)
data class TasteEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "wine_id") val wineId: String,
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
    val acidity: String,
    val tannin: String,
    val body: String,
    val alcohol: String,
    val flavourIntensity: String,
    val flavourCharacteristics: String,
    val finish: String,
    val otherPalate: String,

    // Overall
    val rating: Int,
    val overallImpression: String,
    val bestWith: String,
    val wouldDrinkAgain: Boolean,

    // Metadata
    val date: Long,
    val place: String,
    val latitude: Double?,
    val longitude: Double?
)

fun TasteEntity.toDomain(): Taste = Taste(
    id = id,
    clarity = WineClarity.valueOf(clarity),
    colourIntensity = WineColourIntensity.valueOf(colourIntensity),
    colour = TasteWineColour.valueOf(colour),
    otherVisual = otherVisual,
    noseIntensity = Intensity.valueOf(noseIntensity),
    noseAroma = noseAroma,
    noseDevelopment = NoseDevelopment.valueOf(noseDevelopment),
    otherNose = otherNose,
    sweetness = WineSweetness.valueOf(sweetness),
    acidity = Level.valueOf(acidity),
    tannin = Level.valueOf(tannin),
    body = Level.valueOf(body),
    alcohol = Level.valueOf(alcohol),
    flavourIntensity = Intensity.valueOf(flavourIntensity),
    flavourCharacteristics = flavourCharacteristics,
    finish = Level.valueOf(finish),
    otherPalate = otherPalate,
    rating = rating,
    overallImpression = overallImpression,
    bestWith = bestWith,
    date = Date(date),
    wineId = wineId,
    place = place,
    wouldDrinkAgain = wouldDrinkAgain
)

fun Taste.toEntity(id: String): TasteEntity = TasteEntity(
    id = id,
    wineId = wineId,
    clarity = clarity.name,
    colourIntensity = colourIntensity.name,
    colour = colour.name,
    otherVisual = otherVisual,
    noseIntensity = noseIntensity.name,
    noseAroma = noseAroma,
    noseDevelopment = noseDevelopment.name,
    otherNose = otherNose,
    sweetness = sweetness.name,
    acidity = acidity.name,
    tannin = tannin.name,
    body = body.name,
    alcohol = alcohol.name,
    flavourIntensity = flavourIntensity.name,
    flavourCharacteristics = flavourCharacteristics,
    finish = finish.name,
    otherPalate = otherPalate,
    rating = rating,
    overallImpression = overallImpression,
    bestWith = bestWith,
    wouldDrinkAgain = wouldDrinkAgain,
    date = date.time,
    place = place,
    latitude = null,
    longitude = null
)