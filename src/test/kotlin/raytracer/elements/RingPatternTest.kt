package raytracer.elements

import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.white
import kotlin.test.assertEquals

class RingPatternTest {

    @Test
    fun testRingShouldExtendInBothXandZ() {
        val pattern = RingPattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(black, pattern.patternAt(point(1.0, 0.0, 0.0)))
        assertEquals(black, pattern.patternAt(point(0.0, 0.0, 1.0)))
        assertEquals(black, pattern.patternAt(point(0.708, 0.0, 0.708)))
    }

}
