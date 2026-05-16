package hu.toliver.vinotes.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.toliver.vinotes.data.local.converters.EnumConverter.enumOrNullFromString
import hu.toliver.vinotes.domain.model.Wine
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness

@Entity(tableName = "wines")
data class WineEntity (
    @PrimaryKey val id: String,
    val name: String,
    val producer: String,
    val year: Int,
    val grape: String,
    val isCuvee: Boolean,
    @ColumnInfo(name = "cuvee_components")
    val cuveeComponents: String,
    val country: String,
    val region: String,
    val colour: String,
    val sweetness: String,
    val sugar: Float,
    val alcoholPercentage: Double,
    val volume: Int,
    val description: String,
    val imageUrl: String,
    val isFromCatalog: Boolean = false
)

fun WineEntity.toDomain(): Wine = Wine(
    id = id,
    name = name,
    producer = producer,
    year = year,
    grape = grape,
    isCuvee = isCuvee,
    cuveeComponents = if (cuveeComponents.isBlank()) emptyList()
    else cuveeComponents.split(","),
    colour = enumOrNullFromString<WineColour>(colour)
        ?: WineColour.WHITE, // statistically white is a better choice
    sugar = sugar,
    sweetness = enumOrNullFromString<WineSweetness>(sweetness)
        ?: WineSweetness.DRY,
    country = country,
    region = region,
    alcoholPercentage = alcoholPercentage,
    volume = volume,
    description = description,
    image = imageUrl
)

fun Wine.toEntity(isFromCatalog: Boolean = false): WineEntity = WineEntity(
    id = id,
    name = name,
    producer = producer,
    year = year,
    grape = grape,
    isCuvee = isCuvee,
    cuveeComponents = cuveeComponents.joinToString(","),
    colour = colour.name,
    sugar = sugar,
    sweetness = sweetness.name,
    country = country,
    region = region,
    alcoholPercentage = alcoholPercentage,
    volume = volume,
    description = description,
    imageUrl = image,
    isFromCatalog = isFromCatalog
)