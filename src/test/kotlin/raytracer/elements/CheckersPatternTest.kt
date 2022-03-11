package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.white

internal class CheckersPatternTest {

    @Test
    fun testCheckersRepeatInX() {
        val pattern = CheckersPattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.99, 0.0, 0.0)))
        assertEquals(black, pattern.patternAt(point(1.01, 0.0, 0.0)))
    }

    @Test
    fun testCheckersRepeatInY() {
        val pattern = CheckersPattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.0, 0.99, 0.0)))
        assertEquals(black, pattern.patternAt(point(0.0, 1.01, 0.0)))
    }

    @Test
    fun testCheckersRepeatInZ() {
        val pattern = CheckersPattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.99)))
        assertEquals(black, pattern.patternAt(point(0.0, 0.0, 1.01)))
    }

}
