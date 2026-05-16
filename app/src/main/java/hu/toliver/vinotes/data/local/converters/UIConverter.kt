package hu.toliver.vinotes.data.local.converters

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.domain.model.enums.TasteWineColour
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineSweetness

object UIConverter {
    fun String.toComposeColor(): Color = Color(this.toColorInt())

    fun Level.toFloat(): Float = when (this) {
        Level.LOW -> 0.2f
        Level.MILD -> 0.4f
        Level.MEDIUM -> 0.5f
        Level.SOLID -> 0.75f
        Level.HIGH -> 1.0f
    }

    fun NoseDevelopment.toDisplayName(): String = when (this) {
        NoseDevelopment.YOUTHFUL -> "Youthful"
        NoseDevelopment.DEVELOPING -> "Developing"
        NoseDevelopment.DEVELOPED -> "Developed"
        NoseDevelopment.AGED -> "Aged"
    }

    fun WineClarity.toDisplayName(): String = when (this) {
        WineClarity.CLEAR -> "Clear"
        WineClarity.HAZY -> "Hazy"
        WineClarity.CLOUDY -> "Cloudy"
    }

    fun WineColour.fromColourToHex(): String = when (this) {
        WineColour.GRAY -> "#A0AAB4"
        WineColour.ORANGE -> "#E8A041"
        WineColour.WHITE -> "#F9E5A0"
        WineColour.YELLOW -> "#E8C441"
        WineColour.ROSE -> "#E8A0B4"
        WineColour.SHILLER -> "#D08050"
        WineColour.TAWNY -> "#B8860B"
        WineColour.RED -> "#8B2C2C"
    }

    fun WineColour.toDisplayName(): String = when (this) {
        WineColour.GRAY -> "Gray"
        WineColour.ORANGE -> "Orange"
        WineColour.WHITE -> "White"
        WineColour.YELLOW -> "Yellow"
        WineColour.ROSE -> "Rosé"
        WineColour.SHILLER -> "Shiller"
        WineColour.TAWNY -> "Tawny"
        WineColour.RED -> "Red"
    }


    fun WineSweetness.toDisplayName(): String = when (this) {
        WineSweetness.BRUT_NATURE -> "Brut Nature"
        WineSweetness.EXTRA_BRUT -> "Extra Brut"
        WineSweetness.BRUT -> "Brut"
        WineSweetness.EXTRA_DRY -> "Extra Dry"
        WineSweetness.DRY -> "Dry"
        WineSweetness.SEMI_DRY -> "Semi Dry"
        WineSweetness.SWEET -> "Sweet"
        WineSweetness.UNKNOWN -> "Unknown"
    }

    fun TasteWineColour.toHexColour(): Color  = when (this) {
        TasteWineColour.WHITE -> Color(0xFFF5F0A0)
        TasteWineColour.LEMONGREEN -> Color(0xFFFFF8DC)
        TasteWineColour.LEMON -> Color(0xFFEDD760)
        TasteWineColour.GOLD -> Color(0xFFD4A843)
        TasteWineColour.AMBER -> Color(0xFFCB8A2A)
        TasteWineColour.WHITEBROWN -> Color(0xFFD2B48C)

        TasteWineColour.ROSE -> Color(0xFFFFB6C1)
        TasteWineColour.PINK -> Color(0xFFFFC0CB)
        TasteWineColour.SALMON -> Color(0xFFFA8072)
        TasteWineColour.ORANGE -> Color(0xFFF4A460)

        TasteWineColour.RED -> Color(0xFFCD5C5C)
        TasteWineColour.PURPLE -> Color(0xFF9932CC)
        TasteWineColour.RUBY -> Color(0xFF9B1D30)
        TasteWineColour.GARNET -> Color(0xFF6B1F2A)
        TasteWineColour.TAWNY -> Color(0xFF8B4513)
        TasteWineColour.REDBROWN -> Color(0xFF8B3A3A)
    }
}