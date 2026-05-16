package hu.toliver.vinotes.ui.screen.stats.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AnimatedStatCounter(
    targetValue: Int,
    label: String,
    modifier: Modifier = Modifier,
    suffix: String = "",
    playAnimations: Boolean = true,
    onPlayed: (() -> Unit)? = null,
) {
    var started by remember { mutableStateOf(false) }
    val animatedTarget = if (playAnimations) {
        if (started) targetValue else 0
    } else {
        targetValue
    }
    val animValue by animateIntAsState(
        targetValue = animatedTarget,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
    )
    LaunchedEffect(playAnimations) {
        if (playAnimations) {
            started = true
            onPlayed?.invoke()
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$animValue$suffix",
            style = typography.displaySmall,
            color = colorScheme.primary
        )
        Text(
            text = label,
            style = typography.labelSmall,
            color = colorScheme.onSurfaceVariant
        )
    }
}

