package hu.toliver.vinotes.data.local.converters

import androidx.room.TypeConverter

object EnumConverter {
    @TypeConverter
    inline fun <reified T : Enum<T>> enumOrNullFromString(str: String?): T? {
        if (str.isNullOrBlank()) return null
        return enumValues<T>().firstOrNull { it.name == str.uppercase() }
    }

    @TypeConverter
    fun <T : Enum<T>> toString(enumValue: T): String =
        enumValue.name
}