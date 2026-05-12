package hu.toliver.winotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import hu.toliver.winotes.ui.theme.WiNotesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainActivityContent()
        }
    }
}

@Preview
@Composable
private fun MainActivityContent() {
    WiNotesTheme() {
        //AppNavigation(modifier = Modifier.safeDrawingPadding())
    }
}