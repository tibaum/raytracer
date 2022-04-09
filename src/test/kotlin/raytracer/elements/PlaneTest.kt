package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY

class PlaneTest {

    @Test
    fun testNormalAt() {
        val plane = Plane()
        assertEquals(vector(0.0, 1.0, 0.0), plane.localNormalAt(point(0.0, 0.0, 0.0)))
        assertEquals(vector(0.0, 1.0, 0.0), plane.localNormalAt(point(10.0, 0.0, -10.0)))
        assertEquals(vector(0.0, 1.0, 0.0), plane.localNormalAt(point(-5.0, 0.0, 150.0)))
    }

    @Test
    fun testIntersectWithRayParallelToPlane() {
        val plane = Plane()
        val ray = Ray(point(0.0, 10.0, 0.0), vector(0.0, 0.0, 1.0))
        val intersections = plane.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectWithCoplanarRay() {
        val plane = Plane()
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val intersections = plane.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testRayIntersectPlaneFromAbove() {
        val plane = Plane()
        val ray = Ray(point(0.0, 1.0, 0.0), vector(0.0, -1.0, 0.0))
        val intersections = plane.localIntersect(ray)
        assertEquals(1, intersections.count)
        assertEquals(1.0, intersections[0].time)
        assertEquals(plane, intersections[0].shape)
    }

    @Test
    fun testRayIntersectPlaneFromBelow() {
        val plane = Plane()
        val ray = Ray(point(0.0, -1.0, 0.0), vector(0.0, 1.0, 0.0))
        val intersections = plane.localIntersect(ray)
        assertEquals(1, intersections.count)
        assertEquals(1.0, intersections[0].time)
        assertEquals(plane, intersections[0].shape)
    }

    @Test
    fun testBoundingBox() {
        val plane = Plane()
        val boundingBox = plane.localBoundingBox()
        assertEquals(NEGATIVE_INFINITY, boundingBox.min[0])
        assertEquals(0.0, boundingBox.min[1])
        assertEquals(NEGATIVE_INFINITY, boundingBox.min[2])
        assertEquals(1.0, boundingBox.min[3])
        assertEquals(POSITIVE_INFINITY, boundingBox.max[0])
        assertEquals(0.0, boundingBox.max[1])
        assertEquals(POSITIVE_INFINITY, boundingBox.max[2])
        assertEquals(1.0, boundingBox.max[3])
    }

}
