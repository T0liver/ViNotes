package hu.toliver.vinotes.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val ViNotesShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // chip, badge
    small       = RoundedCornerShape(8.dp),   // button, text field
    medium      = RoundedCornerShape(12.dp),  // cards
    large       = RoundedCornerShape(16.dp),  // bottom sheet, modal
    extraLarge  = RoundedCornerShape(28.dp),  // FAB
)