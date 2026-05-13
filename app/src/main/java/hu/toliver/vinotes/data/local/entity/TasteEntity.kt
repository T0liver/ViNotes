package hu.toliver.vinotes.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.toliver.vinotes.data.local.converters.EnumConverter.enumOrNullFromString
import hu.toliver.vinotes.domain.model.Taste
import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.domain.model.enums.TasteWineColour
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.domain.model.enums.WineSweetness
import java.util.Date

@Entity(
    tableName = "tastings",
    foreignKeys = [ForeignKey(
        entity = WineEntity::class,
        parentColumns = ["id"],
        childColumns = ["wine_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("wine_id")]
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
    clarity = enumOrNullFromString<WineClarity>(clarity)
        ?: WineClarity.CLEAR,
    colourIntensity = enumOrNullFromString<WineColourIntensity>(colourIntensity)
        ?: WineColourIntensity.MEDIUM,
    colour = enumOrNullFromString<TasteWineColour>(colour)
        ?: TasteWineColour.WHITE,
    otherVisual = otherVisual,
    noseIntensity = enumOrNullFromString<Intensity>(noseIntensity)
        ?: Intensity.MEDIUM,
    noseAroma = noseAroma,
    noseDevelopment = enumOrNullFromString<NoseDevelopment>(noseDevelopment)
        ?: NoseDevelopment.DEVELOPED,
    otherNose = otherNose,
    sweetness = enumOrNullFromString<WineSweetness>(sweetness)
        ?: WineSweetness.DRY,
    acidity = enumOrNullFromString<Level>(acidity)
        ?: Level.MEDIUM,
    tannin = enumOrNullFromString<Level>(tannin)
        ?: Level.MEDIUM,
    body = enumOrNullFromString<Level>(body)
        ?: Level.MEDIUM,
    alcohol = enumOrNullFromString<Level>(alcohol)
        ?: Level.MEDIUM,
    flavourIntensity = enumOrNullFromString<Intensity>(flavourIntensity)
        ?: Intensity.MEDIUM,
    flavourCharacteristics = flavourCharacteristics,
    finish = enumOrNullFromString<Level>(finish)
        ?: Level.MEDIUM,
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