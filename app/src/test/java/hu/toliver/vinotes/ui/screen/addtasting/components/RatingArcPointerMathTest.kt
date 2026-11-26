package hu.toliver.vinotes.ui.screen.addtasting.components

import org.junit.Assert.assertEquals
import org.junit.Test

class RatingArcPointerMathTest {

  private val centerX = 540f
  private val centerY = 400f

  @Test
  fun leftEndOfArc_mapsToZero() {
    assertEquals(0, ratingFromPointer(0f, centerY, centerX, centerY))
  }

  @Test
  fun rightEndOfArc_mapsToHundred() {
    assertEquals(100, ratingFromPointer(centerX * 2f, centerY, centerX, centerY))
  }

  @Test
  fun topOfArc_mapsToFifty() {
    assertEquals(50, ratingFromPointer(centerX, centerY - 200f, centerX, centerY))
  }

  @Test
  fun belowArcLeft_clampsToZero_notWrapToHundred() {
    assertEquals(0, ratingFromPointer(centerX - 100f, centerY + 80f, centerX, centerY))
  }

  @Test
  fun belowArcRight_clampsToHundred() {
    assertEquals(100, ratingFromPointer(centerX + 100f, centerY + 80f, centerX, centerY))
  }

  @Test
  fun gapRegionJustPastZeroSide_staysAtZero() {
    val justPastLeft = ratingFromPointer(centerX - 200f, centerY + 1f, centerX, centerY)
    assertEquals(0, justPastLeft)
  }

  @Test
  fun gapRegionJustPastHundredSide_staysAtHundred() {
    val justPastRight = ratingFromPointer(centerX + 200f, centerY + 1f, centerX, centerY)
    assertEquals(100, justPastRight)
  }
}