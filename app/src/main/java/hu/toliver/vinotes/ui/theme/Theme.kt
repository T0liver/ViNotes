package hu.toliver.vinotes.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// ── Light ColorScheme ─────────────────────────────────────────────────────────
private val LightColors = lightColorScheme(
    primary            = Burgundy500,
    onPrimary          = Cream50,
    primaryContainer   = Burgundy200,
    onPrimaryContainer = Burgundy900,

    secondary          = Gold400,
    onSecondary        = WarmGray800,
    secondaryContainer = Gold100,
    onSecondaryContainer = Gold600,

    background         = Cream50,
    onBackground       = WarmGray800,
    surface            = Cream100,
    onSurface          = WarmGray800,
    onSurfaceVariant   = WarmGray400,

    error              = ErrorRed,
)

// ── Dark ColorScheme ──────────────────────────────────────────────────────────
private val DarkColors = darkColorScheme(
    primary            = Burgundy200,
    onPrimary          = Burgundy900,
    primaryContainer   = Burgundy700,
    onPrimaryContainer = Burgundy100,

    secondary          = Gold100,
    onSecondary        = WarmGray800,
    secondaryContainer = Gold600,
    onSecondaryContainer = Gold100,

    background         = DarkBg,
    onBackground       = Cream50,
    surface            = DarkSurface,
    onSurface          = Cream100,
    onSurfaceVariant   = WarmGray400,

    error              = ErrorRed,
)

@Composable
fun ViNotesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else           dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else      -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = ViNotesTypography,
        shapes      = ViNotesShapes,
        content     = content,
    )
}