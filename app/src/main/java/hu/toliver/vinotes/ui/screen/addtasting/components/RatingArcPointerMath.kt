package hu.toliver.vinotes.ui.screen.addtasting.components

import kotlin.math.atan2
import kotlin.math.roundToInt

/**
 * Maps a drag position on the rating arc canvas to a 0–100 value.
 * Valid arc angles are 0° (right, rating 100) through 180° (left, rating 0).
 * Pointer positions below the arc (negative angle) clamp by horizontal side.
 */
fun ratingFromPointer(
    pointerX: Float,
    pointerY: Float,
    centerX: Float,
    centerY: Float,
): Int {
    val degrees = Math.toDegrees(
        atan2(
            (centerY - pointerY).toDouble(),
            (pointerX - centerX).toDouble(),
        ),
    )
    if (degrees < 0.0) {
        return if (pointerX < centerX) 0 else 100
    }
    return (100.0 - degrees / 180.0 * 100.0).roundToInt().coerceIn(0, 100)
}