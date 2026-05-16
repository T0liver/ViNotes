package hu.toliver.vinotes.ui.screen.settings.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ClearDataConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Clear all data?") },
        text = { Text("This action cannot be undone!") },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Back") }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    "Delete",
                    color = colorScheme.error,
                )
            }
        }
    )
}