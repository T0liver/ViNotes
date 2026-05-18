package hu.toliver.vinotes.data.local.converters

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.enums.Intensity
import hu.toliver.vinotes.domain.model.enums.Level
import hu.toliver.vinotes.domain.model.enums.NoseDevelopment
import hu.toliver.vinotes.domain.model.enums.TasteWineColour
import hu.toliver.vinotes.domain.model.enums.WineClarity
import hu.toliver.vinotes.domain.model.enums.WineColour
import hu.toliver.vinotes.domain.model.enums.WineColourIntensity
import hu.toliver.vinotes.domain.model.enums.WineSweetness

object UIConverter {
    fun String.toComposeColor(): Color = Color(this.toColorInt())

    fun Intensity.toDisplayName(): String = when (this) {
        Intensity.NONE -> R.string.none.toString()
        Intensity.LIGHT -> R.string.light.toString()
        Intensity.SHORT -> R.string.tshort.toString()
        Intensity.MEDIUM -> R.string.medium.toString()
        Intensity.LARGE -> R.string.large.toString()
        Intensity.PRONOUNCED -> R.string.pronounced.toString()
    }

    fun Level.toFloat(): Float = when (this) {
        Level.LOW -> 0.2f
        Level.MILD -> 0.4f
        Level.MEDIUM -> 0.5f
        Level.SOLID -> 0.75f
        Level.HIGH -> 1.0f
    }

    fun Level.toDisplayName(): String = when (this) {
        Level.LOW -> R.string.low.toString()
        Level.MILD -> R.string.mild.toString()
        Level.MEDIUM -> R.string.medium.toString()
        Level.SOLID -> R.string.solid.toString()
        Level.HIGH -> R.string.high.toString()
    }

    fun NoseDevelopment.toDisplayName(): String = when (this) {
        NoseDevelopment.YOUTHFUL -> R.string.youthful.toString()
        NoseDevelopment.DEVELOPING -> R.string.developing.toString()
        NoseDevelopment.DEVELOPED -> R.string.developed.toString()
        NoseDevelopment.AGED -> R.string.aged.toString()
    }

    fun WineClarity.toDisplayName(): String = when (this) {
        WineClarity.CLEAR -> R.string.clear.toString()
        WineClarity.HAZY -> R.string.hazy.toString()
        WineClarity.CLOUDY -> R.string.cloudy.toString()
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
        WineColour.GRAY -> R.string.gray.toString()
        WineColour.ORANGE -> R.string.orange.toString()
        WineColour.WHITE -> R.string.white.toString()
        WineColour.YELLOW -> R.string.yellow.toString()
        WineColour.ROSE -> R.string.rose.toString()
        WineColour.SHILLER -> R.string.shiller.toString()
        WineColour.TAWNY -> R.string.tawny.toString()
        WineColour.RED -> R.string.red.toString()
    }


    fun WineSweetness.toDisplayName(): String = when (this) {
        WineSweetness.BRUT_NATURE -> R.string.brut_nature.toString()
        WineSweetness.EXTRA_BRUT -> R.string.extra_brut.toString()
        WineSweetness.BRUT -> R.string.brut.toString()
        WineSweetness.EXTRA_DRY -> R.string.extra_dry.toString()
        WineSweetness.DRY -> R.string.dry.toString()
        WineSweetness.SEMI_DRY -> R.string.semi_dry.toString()
        WineSweetness.SWEET -> R.string.sweet.toString()
        WineSweetness.UNKNOWN -> R.string.unknown.toString()
    }

    fun WineColourIntensity.toDisplayName(): String = when (this) {
        WineColourIntensity.PALE -> R.string.pale.toString()
        WineColourIntensity.MEDIUM -> R.string.medium.toString()
        WineColourIntensity.DEEP -> R.string.deep.toString()
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