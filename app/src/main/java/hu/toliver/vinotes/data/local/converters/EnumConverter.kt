package hu.toliver.vinotes.data.local.converters

import androidx.room.TypeConverter
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness

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

    @TypeConverter
    fun WineColour.colorHex(): String = when (this) {
        WineColour.GRAY -> "#A0AAB4"
        WineColour.ORANGE -> "#E8A041"
        WineColour.WHITE -> "#F9E5A0"
        WineColour.YELLOW -> "#E8C441"
        WineColour.ROSE -> "#E8A0B4"
        WineColour.SHILLER -> "#D08050"
        WineColour.TAWNY -> "#B8860B"
        WineColour.RED -> "#8B2C2C"
    }

    @TypeConverter
    fun WineColour.displayName(): String = when (this) {
        WineColour.GRAY -> "Szürke"
        WineColour.ORANGE -> "Narancs"
        WineColour.WHITE -> "Fehér"
        WineColour.YELLOW -> "Sárga"
        WineColour.ROSE -> "Rozé"
        WineColour.SHILLER -> "Rotgold"
        WineColour.TAWNY -> "Tawny"
        WineColour.RED -> "Vörös"
    }


    fun WineSweetness.displayName () : String = when (this) {
        WineSweetness.BRUT_NATURE -> "Brut Nature"
        WineSweetness.EXTRA_BRUT -> "Extra Brut"
        WineSweetness.BRUT -> "Brut"
        WineSweetness.EXTRA_DRY -> "Extra Dry"
        WineSweetness.DRY -> "Száraz"
        WineSweetness.SEMI_DRY -> "Félédes"
        WineSweetness.SWEET -> "Édes"
        WineSweetness.UNKNOWN -> "Ismeretlen"
    }
}