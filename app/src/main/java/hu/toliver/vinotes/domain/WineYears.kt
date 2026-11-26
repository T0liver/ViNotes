package hu.toliver.vinotes.domain

import java.util.Calendar

object WineYears {
    const val MIN_YEAR = 1900

    fun maxYear(): Int = Calendar.getInstance().get(Calendar.YEAR) + 1

    fun isValid(year: Int): Boolean = year in MIN_YEAR..maxYear()
}