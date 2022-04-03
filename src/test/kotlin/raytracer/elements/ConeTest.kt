package raytracer.elements

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.math.abs
import kotlin.math.sqrt

internal class ConeTest {

    @Test
    fun testInfiniteConeCannotBeClosed() {
        Assertions.assertThrows(IllegalArgumentException::class.java) { Cone(closed = true) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Cone(minimum = 1.0, closed = true) }
        Assertions.assertThrows(IllegalArgumentException::class.java) { Cone(maximum = 1.0, closed = true) }
    }

    @Test
    fun testIntersectRayWithCone() {
        val cone = Cone()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(5.0, intersections[0].time)
        assertEquals(5.0, intersections[1].time)
    }

    @Test
    fun testIntersectRayWithCone2() {
        val cone = Cone()
        val ray = Ray(point(0.0, 0.0, -5.0), vector(1.0, 1.0, 1.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(2, intersections.count)
        assertTrue(abs(8.66025 - intersections[0].time) < EPSILON)
        assertTrue(abs(8.66025 - intersections[1].time) < EPSILON)
    }

    @Test
    fun testIntersectRayWithCone3() {
        val cone = Cone()
        val ray = Ray(point(1.0, 1.0, -5.0), vector(-0.5, -1.0, 1.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(2, intersections.count)
        assertTrue(abs(4.55006 - intersections[0].time) < EPSILON)
        assertTrue(abs(49.44994 - intersections[1].time) < EPSILON)
    }

    @Test
    fun testIntersectRayParallelToOneOfTheHalves() {
        val cone = Cone()
        val ray = Ray(point(0.0, 0.0, -1.0), vector(0.0, 1.0, 1.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(1, intersections.count)
        assertTrue(abs(0.35355 - intersections[0].time) < EPSILON)
    }

    @Test
    fun testIntersectCaps() {
        val cone = Cone(minimum = -0.5, maximum = 0.5, closed = true)
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 1.0, 0.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectCaps2() {
        val cone = Cone(minimum = -0.5, maximum = 0.5, closed = true)
        val ray = Ray(point(0.0, 0.0, -0.25), vector(0.0, 1.0, 1.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(2, intersections.count)
    }

    @Test
    fun testIntersectCaps3() {
        val cone = Cone(minimum = -0.5, maximum = 0.5, closed = true)
        val ray = Ray(point(0.0, 0.0, -0.25), vector(0.0, 1.0, 0.0).normalize())
        val intersections = cone.intersect(ray)
        assertEquals(4, intersections.count)
    }

    @Test
    fun testIntersectCapsOfClosedConeFromAbovePerpendicular() {
        val cone = Cone(minimum = 0.0, maximum = 1.5, closed = true)
        val ray = Ray(point(1.5, 3.0, 0.0), vector(0.0, -1.0, 0.0).normalize())
        val intersections = cone.localIntersect(ray)
        assertEquals(1, intersections.count)
    }

    @Test
    fun testNormal() {
        val cone = Cone()
        assertEquals(vector(0.0, 0.0, 0.0), cone.localNormalAt(point(0.0, 0.0, 0.0)))
        assertEquals(vector(1.0, -sqrt(2.0), 1.0), cone.localNormalAt(point(1.0, 1.0, 1.0)))
        assertEquals(vector(-1.0, 1.0, 0.0), cone.localNormalAt(point(-1.0, -1.0, 0.0)))
    }

    @Test
    fun testNormalOnEndCapsLowerSide() {
        val cone = Cone(minimum = -2.0, maximum = 2.0, closed = true)
        assertEquals(vector(0.0, -1.0, 0.0), cone.localNormalAt(point(0.0, -2.0, 0.0)))
        assertEquals(vector(0.0, -1.0, 0.0), cone.localNormalAt(point(1.5, -2.0, 0.0)))
        assertEquals(vector(0.0, -1.0, 0.0), cone.localNormalAt(point(0.0, -2.0, 1.5)))
    }

    @Test
    fun testNormalOnEndCapsUpperSide() {
        val cone = Cone(minimum = -2.0, maximum = 2.0, closed = true)
        assertEquals(vector(0.0, 1.0, 0.0), cone.localNormalAt(point(0.0, 2.0, 0.0)))
        assertEquals(vector(0.0, 1.0, 0.0), cone.localNormalAt(point(1.5, 2.0, 0.0)))
        assertEquals(vector(0.0, 1.0, 0.0), cone.localNormalAt(point(0.0, 2.0, 1.5)))
    }

}
