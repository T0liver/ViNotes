package hu.toliver.vinotes.data.local.converters

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

object ListConverter {
    private val json = Json

    @TypeConverter
    fun jsonFromString(value: String): List<String> =
        json.decodeFromString(value)

    @TypeConverter
    fun jsonToString(list: List<String>): String =
        json.encodeToString(list)

    @TypeConverter
    fun fromStringList(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(",")

    @TypeConverter
    fun toStringList(list: List<String>): String =
        list.joinToString(",")
}