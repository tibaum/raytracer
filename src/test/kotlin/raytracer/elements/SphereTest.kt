package raytracer.elements

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import kotlin.math.sqrt

class SphereTest {

    @Test
    fun testDefaultTransformation() {
        val sphere = Sphere()
        assertEquals(Matrix.identity(4), sphere.transformationMatrix)
    }

    @Test
    fun testChangingTranformation() {
        val translation = Matrix.translation(2.0, 3.0, 4.0)
        val sphere = Sphere(translation)
        assertEquals(translation, sphere.transformationMatrix)
    }

    @Test
    fun testNormalOnXAxis() {
        val sphere = Sphere()
        val normal = sphere.localNormalAt(point(1.0, 0.0, 0.0))
        assertEquals(Tuple.vector(1.0, 0.0, 0.0), normal)
    }

    @Test
    fun testNormalOnYAxis() {
        val sphere = Sphere()
        val normal = sphere.localNormalAt(point(0.0, 1.0, 0.0))
        assertEquals(Tuple.vector(0.0, 1.0, 0.0), normal)
    }

    @Test
    fun testNormalOnZAxis() {
        val sphere = Sphere()
        val normal = sphere.localNormalAt(point(0.0, 0.0, 1.0))
        assertEquals(Tuple.vector(0.0, 0.0, 1.0), normal)
    }

    @Test
    fun testNormalAtNonAxialPoint() {
        val sphere = Sphere()
        val d = sqrt(3.0) / 3.0
        val normal = sphere.localNormalAt(point(d, d, d))
        assertEquals(Tuple.vector(d, d, d), normal)
    }

    @Test
    fun testComputeIntersection() {
        val origin = point(0.0, 0.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = sphere.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(4.0, intersections[0].time)
        assertEquals(6.0, intersections[1].time)
    }

    @Test
    fun testComputeIntersectionTangent() {
        val origin = point(0.0, 1.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = sphere.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(5.0, intersections[0].time)
        assertEquals(5.0, intersections[1].time)
    }

    @Test
    fun testComputeIntersectionWhenRayMissesSphere() {
        val origin = point(0.0, 2.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = sphere.localIntersect(ray)
        assertEquals(0, intersections.count)
    }

    @Test
    fun testComputeIntersectionWhenRayStartsInsideSphere() {
        val origin = point(0.0, 0.0, 0.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = sphere.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(-1.0, intersections[0].time)
        assertEquals(1.0, intersections[1].time)
    }

    @Test
    fun testComputeIntersectionWhenSphereIsBehindRay() {
        val origin = point(0.0, 0.0, 5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = sphere.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(-6.0, intersections[0].time)
        assertEquals(-4.0, intersections[1].time)
    }

    @Test
    fun testIntersectSetsObjectOnTheIntersection() {
        val origin = point(0.0, 0.0, -5.0)
        val direction = Tuple.vector(0.0, 0.0, 1.0)
        val ray = Ray(origin, direction)
        val sphere = Sphere()
        val intersections = sphere.localIntersect(ray)
        assertEquals(2, intersections.count)
        assertEquals(sphere, intersections[0].shape)
        assertEquals(sphere, intersections[1].shape)
    }

    @Test
    fun testBoundingBox() {
        val sphere = Sphere()
        val boundingBox = sphere.localBoundingBox()
        assertEquals(point(-1.0, -1.0, -1.0), boundingBox.min)
        assertEquals(point(1.0, 1.0, 1.0), boundingBox.max)
    }

}
