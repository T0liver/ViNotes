package hu.toliver.vinotes.data.local.converters

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import java.util.Date

object DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? =
        value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? =
        date?.time

    @TypeConverter
    fun localDateToString(localDate: LocalDate): String = localDate.toString()

    @TypeConverter
    fun stringToLocalDate(string : String): LocalDate = LocalDate.parse(string)
}