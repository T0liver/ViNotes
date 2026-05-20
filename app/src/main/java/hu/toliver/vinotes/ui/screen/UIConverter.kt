package hu.toliver.vinotes.ui.screen

import android.content.Context
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
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


    @StringRes
    fun Intensity.displayNameRes(): Int = when (this) {
        Intensity.NONE -> R.string.none
        Intensity.LIGHT -> R.string.light
        Intensity.SHORT -> R.string.tshort
        Intensity.MEDIUM -> R.string.medium
        Intensity.LARGE -> R.string.large
        Intensity.PRONOUNCED -> R.string.pronounced
    }

    @Composable
    fun Intensity.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun Intensity.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun Intensity.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())

    fun Level.toFloat(): Float = when (this) {
        Level.LOW -> 0.2f
        Level.MILD -> 0.4f
        Level.MEDIUM -> 0.5f
        Level.SOLID -> 0.75f
        Level.HIGH -> 1.0f
    }

    @StringRes
    fun Level.displayNameRes(): Int = when (this) {
        Level.LOW -> R.string.low
        Level.MILD -> R.string.mild
        Level.MEDIUM -> R.string.medium
        Level.SOLID -> R.string.solid
        Level.HIGH -> R.string.high
    }

    @Composable
    fun Level.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun Level.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun Level.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())

    @StringRes
    fun NoseDevelopment.displayNameRes(): Int = when (this) {
        NoseDevelopment.YOUTHFUL -> R.string.youthful
        NoseDevelopment.DEVELOPING -> R.string.developing
        NoseDevelopment.DEVELOPED -> R.string.developed
        NoseDevelopment.AGED -> R.string.aged
    }

    @Composable
    fun NoseDevelopment.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun NoseDevelopment.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun NoseDevelopment.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())

    @StringRes
    fun WineClarity.displayNameRes(): Int = when (this) {
        WineClarity.CLEAR -> R.string.clear
        WineClarity.HAZY -> R.string.hazy
        WineClarity.CLOUDY -> R.string.cloudy
    }

    @Composable
    fun WineClarity.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun WineClarity.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun WineClarity.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())

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

    @StringRes
    fun WineColour.displayNameRes(): Int = when (this) {
        WineColour.GRAY -> R.string.gray
        WineColour.ORANGE -> R.string.orange
        WineColour.WHITE -> R.string.white
        WineColour.YELLOW -> R.string.yellow
        WineColour.ROSE -> R.string.rose
        WineColour.SHILLER -> R.string.shiller
        WineColour.TAWNY -> R.string.tawny
        WineColour.RED -> R.string.red
    }

    @Composable
    fun WineColour.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun WineColour.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun WineColour.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())


    @StringRes
    fun WineSweetness.displayNameRes(): Int = when (this) {
        WineSweetness.BRUT_NATURE -> R.string.brut_nature
        WineSweetness.EXTRA_BRUT -> R.string.extra_brut
        WineSweetness.BRUT -> R.string.brut
        WineSweetness.EXTRA_DRY -> R.string.extra_dry
        WineSweetness.DRY -> R.string.dry
        WineSweetness.SEMI_DRY -> R.string.semi_dry
        WineSweetness.SWEET -> R.string.sweet
        WineSweetness.UNKNOWN -> R.string.unknown
    }

    @Composable
    fun WineSweetness.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun WineSweetness.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun WineSweetness.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())

    @StringRes
    fun WineColourIntensity.displayNameRes(): Int = when (this) {
        WineColourIntensity.PALE -> R.string.pale
        WineColourIntensity.MEDIUM -> R.string.medium
        WineColourIntensity.DEEP -> R.string.deep
    }

    @Composable
    fun WineColourIntensity.toDisplayName(): String = androidx.compose.ui.res.stringResource(displayNameRes())

    fun WineColourIntensity.toDisplayName(context: Context): String = context.getString(displayNameRes())

    fun WineColourIntensity.toDisplayName(resources: Resources): String = resources.getString(displayNameRes())

    fun TasteWineColour.toHexColour(): Color = when (this) {
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

    fun TasteWineColour.toDisplayName(context: Context): String = when (this) {
        TasteWineColour.WHITE -> context.getString(R.string.white)
        TasteWineColour.LEMONGREEN -> context.getString(R.string.lemongreen)
        TasteWineColour.LEMON -> context.getString(R.string.lemon)
        TasteWineColour.GOLD -> context.getString(R.string.gold)
        TasteWineColour.AMBER -> context.getString(R.string.amber)
        TasteWineColour.WHITEBROWN -> context.getString(R.string.whitebrown)

        TasteWineColour.ROSE -> context.getString(R.string.rose)
        TasteWineColour.PINK -> context.getString(R.string.pink)
        TasteWineColour.SALMON -> context.getString(R.string.salmon)
        TasteWineColour.ORANGE -> context.getString(R.string.orange)

        TasteWineColour.RED -> context.getString(R.string.red)
        TasteWineColour.PURPLE -> context.getString(R.string.purple)
        TasteWineColour.RUBY -> context.getString(R.string.ruby)
        TasteWineColour.GARNET -> context.getString(R.string.garnet)
        TasteWineColour.TAWNY -> context.getString(R.string.tawny)
        TasteWineColour.REDBROWN -> context.getString(R.string.redbrown)
    }
}