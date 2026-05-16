package hu.toliver.vinotes.ui.screen.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.ui.AppConstants

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    onInfoClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        uriHandler.openUri(AppConstants.GITHUB_URL)
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = colorScheme.surfaceVariant,
                            shape = CircleShape,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Code,
                        contentDescription = "GitHub",
                        tint = colorScheme.onSurfaceVariant,
                    )
                }

                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = colorScheme.surfaceVariant,
                            shape = CircleShape,
                        ),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "About info",
                        tint = colorScheme.onSurfaceVariant,
                    )
                }
            }

            HorizontalDivider(color = colorScheme.outlineVariant)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "${AppConstants.APP_AUTHOR} © ${AppConstants.APP_YEAR}",
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "Icons by ",
                    style = typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "Icons8.com",
                    style = typography.bodyMedium.copy(textDecoration = TextDecoration.Underline),
                    color = colorScheme.primary,
                    modifier = Modifier.clickable { uriHandler.openUri(AppConstants.ICONS8_URL) },
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = colorScheme.primary,
                )
            }
        }
    }
}