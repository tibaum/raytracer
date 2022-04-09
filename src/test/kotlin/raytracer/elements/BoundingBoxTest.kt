package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector

internal class BoundingBoxTest {

    @Test
    fun testAccumulate() {
        val box1 = BoundingBox(point(1.0, 3.0, -4.0), point(5.0, 4.0, 1.0))
        val box2 = BoundingBox(point(-7.0, 4.0, -5.0), point(3.0, 8.0, 2.0))
        val box = box1.accumulate(box2)
        assertEquals(point(-7.0, 3.0, -5.0), box.min)
        assertEquals(point(5.0, 8.0, 2.0), box.max)
    }

    @Test
    fun testIntersectFromPositiveX() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(5.0, 2.5, 2.0), vector(-1.0, 0.0, 0.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testIntersectFromNegativeX() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-5.0, 2.5, 2.0), vector(1.0, 0.0, 0.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testIntersectFromPositiveY() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-2.0, 7.0, 2.0), vector(0.0, -1.0, 0.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testIntersectFromNegativeY() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-2.0, -7.0, 2.0), vector(0.0, 1.0, 0.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testIntersectFromPositiveZ() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-2.0, -1.0, 6.0), vector(0.0, 0.0, -1.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testIntersectFromNegativeZ() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-2.0, -1.0, -6.0), vector(0.0, 0.0, 1.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testIntersectFromInside() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-2.0, -1.0, 1.0), vector(0.0, 0.0, 1.0))
        assertTrue(box.intersect(ray))
    }

    @Test
    fun testRayMissesFromNegativeX() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(-5.0, 0.0, 0.0), vector(0.42046, 0.90731, 0.0))
        assertFalse(box.intersect(ray))
    }

    @Test
    fun testRayMissesFromNegativeY() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(0.0, -5.0, 0.0), vector(-0.70273, 0.65739, 0.27202))
        assertFalse(box.intersect(ray))
    }

    @Test
    fun testRayMissesFromNegativeZ() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.13236, -0.55591, 0.82063))
        assertFalse(box.intersect(ray))
    }

    @Test
    fun testRayMissesFromPositiveXAndZ() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(2.1, 0.0, 5.0), vector(0.0, 0.0, -1.0))
        assertFalse(box.intersect(ray))
    }

    @Test
    fun testRayMissesFromPositiveYAndZ() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(0.0, 5.0, 3.1), vector(0.0, -1.0, 0.0))
        assertFalse(box.intersect(ray))
    }

    @Test
    fun testRayMissesFromPositiveXAndY() {
        val box = BoundingBox(point(-3.0, -2.0, -1.0), point(2.0, 4.0, 3.0))
        val ray = Ray(point(1.0, 4.0, 0.0), vector(-1.0, 0.0, 0.0))
        assertFalse(box.intersect(ray))
    }

}
