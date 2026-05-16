package hu.toliver.vinotes.ui.screen.settings.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title.uppercase(),
        style = typography.labelSmall,
        color = colorScheme.primary,
        modifier = modifier.padding(top = 12.dp, bottom = 4.dp, start = 4.dp),
    )
}