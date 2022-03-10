package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.black
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.white

internal class StripePatternTest {

    @Test
    fun testStripePatternIsConstantInY() {
        val pattern = StripePattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.0, 1.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.0, 2.0, 0.0)))
    }

    @Test
    fun testStripePatternIsConstantInZ() {
        val pattern = StripePattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 1.0)))
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 2.0)))
    }

    @Test
    fun testStripePatternAlternatesInX() {
        val pattern = StripePattern()
        assertEquals(white, pattern.patternAt(point(0.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(0.9, 0.0, 0.0)))
        assertEquals(black, pattern.patternAt(point(1.0, 0.0, 0.0)))
        assertEquals(black, pattern.patternAt(point(-0.1, 0.0, 0.0)))
        assertEquals(black, pattern.patternAt(point(-1.0, 0.0, 0.0)))
        assertEquals(white, pattern.patternAt(point(-1.1, 0.0, 0.0)))
    }

    @Test
    fun testStripesWithAnObjectTransformation() {
        val sphere = Sphere(transformationMatrix = Matrix.scaling(2.0, 2.0, 2.0))
        val pattern = StripePattern()
        val color = pattern.patternAtShape(sphere, point(1.5, 0.0, 0.0))
        assertEquals(white, color)
    }

    @Test
    fun testStripesWithAPatternTransformation() {
        val sphere = Sphere()
        val pattern = StripePattern(transformationMatrix = Matrix.scaling(2.0, 2.0, 2.0))
        val color = pattern.patternAtShape(sphere, point(1.5, 0.0, 0.0))
        assertEquals(white, color)
    }

    @Test
    fun testStripesWithBothObjectAndPatternTransformation() {
        val sphere = Sphere(transformationMatrix = Matrix.scaling(2.0, 2.0, 2.0))
        val pattern = StripePattern(transformationMatrix = Matrix.translation(0.5, 0.0, 0.0))
        val color = pattern.patternAtShape(sphere, point(2.5, 0.0, 0.0))
        assertEquals(white, color)
    }

}
