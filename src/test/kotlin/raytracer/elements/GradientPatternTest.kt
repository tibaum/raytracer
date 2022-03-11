package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.color
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.white

internal class GradientPatternTest {

    @Test
    fun testGradientLinearlyInterpolatesBetweenColors() {
        val pattern = GradientPattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(color(0.75, 0.75, 0.75), pattern.patternAt(point(0.25, 0.0, 0.0)))
        assertEquals(color(0.5, 0.5, 0.5), pattern.patternAt(point(0.5, 0.0, 0.0)))
        assertEquals(color(0.25, 0.25, 0.25), pattern.patternAt(point(0.75, 0.0, 0.0)))
    }

}
