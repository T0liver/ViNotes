package hu.toliver.vinotes.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object ListConverter {
    private val json = Json

    @TypeConverter
    fun fromString(value: String): List<String> =
        json.decodeFromString(value)

    @TypeConverter
    fun toString(list: List<String>): String =
        json.encodeToString(list)
}