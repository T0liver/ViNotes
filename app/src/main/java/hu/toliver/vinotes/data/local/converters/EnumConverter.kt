package hu.toliver.vinotes.data.local.converters

import androidx.room.TypeConverter
import hu.toliver.vinotes.domain.model.enums.Level

object EnumConverter {
    @TypeConverter
    inline fun <reified T : Enum<T>> enumOrNullFromString(str: String?): T? {
        if (str.isNullOrBlank()) return null
        return enumValues<T>().firstOrNull { it.name == str.uppercase() }
    }

    @TypeConverter
    fun <T : Enum<T>> toString(enumValue: T): String =
        enumValue.name

    @TypeConverter
    fun Level.toFloat(): Float = when (this) {
        Level.LOW -> 0.2f
        Level.MILD -> 0.4f
        Level.MEDIUM -> 0.5f
        Level.SOLID -> 0.75f
        Level.HIGH -> 1.0f
    }
}