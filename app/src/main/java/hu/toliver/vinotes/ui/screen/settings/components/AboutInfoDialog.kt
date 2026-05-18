package hu.toliver.vinotes.ui.screen.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import hu.toliver.vinotes.R

@Composable
fun AboutInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Text("🍷", style = typography.headlineMedium) },
        title = { Text(stringResource(R.string.app_name)) },
        text = {
            Text(
                text = stringResource(R.string.about_text),
                style = typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.close)) } },
    )
}