package hu.toliver.vinotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import hu.toliver.vinotes.domain.model.enums.AppLanguage
import hu.toliver.vinotes.domain.model.enums.ThemeMode
import hu.toliver.vinotes.ui.navigation.VinNoteNavGraph
import hu.toliver.vinotes.ui.screen.settings.ThemeViewModel
import hu.toliver.vinotes.ui.theme.ViNotesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent()
        }
    }
}

@Preview
@Composable
private fun MainActivityContent() {
    val vm: ThemeViewModel = hiltViewModel()
    val theme by vm.themeMode.collectAsState()
    val language by vm.appLanguage.collectAsState()

    LaunchedEffect(language) {
        AppCompatDelegate.setApplicationLocales(language.toLocaleListCompat())
    }

    val dark = when (theme) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    ViNotesTheme(darkTheme = dark) {
        VinNoteNavGraph()
    }
}

private fun AppLanguage.toLocaleListCompat(): LocaleListCompat =
    when (this) {
        AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
        AppLanguage.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        AppLanguage.HUNGARIAN -> LocaleListCompat.forLanguageTags("hu")
    }
