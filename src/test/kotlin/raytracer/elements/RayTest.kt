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
        val sphere = Sphere()
        val intersections = ray.intersect(sphere)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testComputeIntersectionTangent() {
        val origin = Tuple.point(0.0, 1.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = ray.intersect(sphere)
        assertEquals(2, intersections.count)
        assertEquals(5.0, intersections[0].time)
        assertEquals(5.0, intersections[1].time)
    }

    @Test
    fun testComputeIntersectionWhenRayMissesSphere() {
        val origin = Tuple.point(0.0, 2.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = ray.intersect(sphere)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testComputeIntersectionWhenRayStartsInsideSphere() {
        val origin = Tuple.point(0.0, 0.0, 0.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = ray.intersect(sphere)
        assertEquals(2, intersections.count)
        assertEquals(-1.0, intersections[0].time)
        assertEquals(1.0, intersections[1].time)
    }

    @Test
    fun testComputeIntersectionWhenSphereIsBehindRay() {
        val origin = Tuple.point(0.0, 0.0, 5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = ray.intersect(sphere)
        assertEquals(2, intersections.count)
        assertEquals(-6.0, intersections[0].time)
        assertEquals(-4.0, intersections[1].time)
    }

    @Test
    fun testIntersectSetsObjectOnTheIntersection() {
        val origin = Tuple.point(0.0, 0.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = ray.intersect(sphere)
        assertEquals(2, intersections.count)
        assertEquals(sphere, intersections[0].sphere)
        assertEquals(sphere, intersections[1].sphere)
    }

    @Test
    fun testTranslatingRay() {
        val ray = Ray(Tuple.point(1.0, 2.0, 3.0), Tuple.vector(0.0, 1.0, 0.0))
        val translationMatrix = Matrix.translation(3.0, 4.0, 5.0)
        val rayTransformed: Ray = ray.transformByMatrix(translationMatrix)
        assertEquals(Tuple.point(4.0, 6.0, 8.0), rayTransformed.origin)
        assertEquals(Tuple.vector(0.0, 1.0, 0.0), rayTransformed.direction)
    }

    @Test
    fun testScalingRay() {
        val ray = Ray(Tuple.point(1.0, 2.0, 3.0), Tuple.vector(0.0, 1.0, 0.0))
        val scalingMatrix = Matrix.scaling(2.0, 3.0, 4.0)
        val rayTransformed: Ray = ray.transformByMatrix(scalingMatrix)
        assertEquals(Tuple.point(2.0, 6.0, 12.0), rayTransformed.origin)
        assertEquals(Tuple.vector(0.0, 3.0, 0.0), rayTransformed.direction)
    }

    @Test
    fun testIntersectingScaledSphere() {
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = Sphere(Matrix.scaling(2.0, 2.0, 2.0))
        val intersections = ray.intersect(sphere)
        assertEquals(2, intersections.count)
        assertEquals(3.0, intersections[0].time)
        assertEquals(7.0, intersections[1].time)
    }

    @Test
    fun testIntersectingTranslatedSphere() {
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = Sphere(Matrix.translation(5.0, 0.0, 0.0))
        val intersections = ray.intersect(sphere)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testIntersectingDefaultWorld() {
        val world = World()
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val intersections = ray.intersect(world)
        assertEquals(4, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(4.5, intersections[1].time)
        assertEquals(5.5, intersections[2].time)
        assertEquals(6.0, intersections[3].time)
    }

}
