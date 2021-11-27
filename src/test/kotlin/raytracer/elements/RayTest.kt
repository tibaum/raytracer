package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class RayTest {

    @Test
    fun testConstruction() {
        val origin = Tuple.point(1.0, 2.0, 3.0)
        val direction = Tuple.vector(4.0, 5.0, 6.0)
        val ray = Ray(origin, direction)
        assertEquals(origin, ray.origin)
        assertEquals(direction, ray.direction)
    }

    @Test
    fun testConstructionExceptionWhenOriginIsNotAPoint() {
        val origin = Tuple.vector(1.0, 2.0, 3.0)
        val direction = Tuple.vector(4.0, 5.0, 6.0)
        assertThrows(IllegalArgumentException::class.java) { Ray(origin, direction) }
    }

    @Test
    fun testConstructionExceptionWhenDirectionIsNotAVector() {
        val origin = Tuple.point(1.0, 2.0, 3.0)
        val direction = Tuple.point(4.0, 5.0, 6.0)
        assertThrows(IllegalArgumentException::class.java) { Ray(origin, direction) }
    }

    @Test
    fun testComputePointFromDistance() {
        val origin = Tuple.point(2.0, 3.0, 4.0)
        val direction = Tuple.vector(1.0, 0.0, 0.0)
        val ray = Ray(origin, direction)
        assertEquals(Tuple.point(2.0, 3.0, 4.0), ray.position(0.0))
        assertEquals(Tuple.point(3.0, 3.0, 4.0), ray.position(1.0))
        assertEquals(Tuple.point(1.0, 3.0, 4.0), ray.position(-1.0))
        assertEquals(Tuple.point(4.5, 3.0, 4.0), ray.position(2.5))
    }

    @Test
    fun testComputeIntersection() {
        val origin = Tuple.point(0.0, 0.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere.unit()
        val intersections: List<Double> = ray.intersect(sphere)
        assertEquals(2, intersections.size)
        assertEquals(4.0, intersections[0])
        assertEquals(6.0, intersections[1])
    }

    @Test
    fun testComputeIntersectionTangent() {
        val origin = Tuple.point(0.0, 1.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere.unit()
        val intersections: List<Double> = ray.intersect(sphere)
        assertEquals(2, intersections.size)
        assertEquals(5.0, intersections[0])
        assertEquals(5.0, intersections[1])
    }

    @Test
    fun testComputeIntersectionWhenRayMissesSphere() {
        val origin = Tuple.point(0.0, 2.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere.unit()
        val intersections: List<Double> = ray.intersect(sphere)
        assertEquals(0, intersections.size)
    }

    @Test
    fun testComputeIntersectionWhenRayStartsInsideSphere() {
        val origin = Tuple.point(0.0, 0.0, 0.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere.unit()
        val intersections: List<Double> = ray.intersect(sphere)
        assertEquals(2, intersections.size)
        assertEquals(-1.0, intersections[0])
        assertEquals(1.0, intersections[1])
    }

    @Test
    fun testComputeIntersectionWhenSphereIsBehindRay() {
        val origin = Tuple.point(0.0, 0.0, 5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere.unit()
        val intersections: List<Double> = ray.intersect(sphere)
        assertEquals(2, intersections.size)
        assertEquals(-6.0, intersections[0])
        assertEquals(-4.0, intersections[1])
    }

}
