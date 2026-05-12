package hu.toliver.vinotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import hu.toliver.vinotes.ui.theme.ViNotesTheme

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
    ViNotesTheme() {
        //AppNavigation(modifier = Modifier.safeDrawingPadding())
    }
}