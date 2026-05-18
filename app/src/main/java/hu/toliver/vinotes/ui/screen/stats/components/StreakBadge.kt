package hu.toliver.vinotes.ui.screen.stats.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hu.toliver.vinotes.R
import hu.toliver.vinotes.domain.model.FullStatsData

@Composable
fun StreakSection(data: FullStatsData, modifier: Modifier = Modifier) {
    val hasStreak = data.currentStreak > 0

    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = if (hasStreak) 0.8f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (data.currentStreak >= 7) Color(0xFFFF6B35).copy(alpha = 0.12f) else colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (hasStreak) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = Color(0xFFFF6B35).copy(alpha = glowAlpha * 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                    Text(
                        text = if (hasStreak) "🔥" else "🍷",
                        style = typography.headlineMedium
                    )
                }
                Column {
                    Text(
                        text = if (hasStreak) stringResource(
                            R.string.amount_day_streak,
                            data.currentStreak
                        ) else stringResource(R.string.no_streak_yet),
                        style = typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.taste_today),
                        style = typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = stringResource(R.string.longest),
                    style = typography.labelSmall,
                    color = colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.amount_day, data.longestStreak),
                    style = typography.titleMedium,
                    color = colorScheme.primary
                )
            }
        }
    }
}