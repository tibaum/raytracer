package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raytracer.elements.Tuple.Companion.point
import raytracer.elements.Tuple.Companion.vector
import kotlin.math.sqrt

class ShadingPreComputationTest {

    @Test()
    fun testPrecomputingTheStateOfAnIntersectionOutside() {
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val sphere = Sphere()
        val intersection = Intersection(4.0, sphere)
        val intersectionData = ShadingPreComputation.of(ray, intersection)
        assertEquals(intersection.time, intersectionData.hitTime)
        assertEquals(intersection.shape, intersectionData.shape)
        assertEquals(point(0.0, 0.0, -1.0), intersectionData.intersectionPoint)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.eyeVector)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.normalVector)
        assertEquals(false, intersectionData.intersectionOccursInside)
    }

    @Test()
    fun testPrecomputingTheStateOfAnIntersectionInside() {
        val ray = Ray(point(0.0, 0.0, 0.0), vector(0.0, 0.0, 1.0))
        val sphere = Sphere()
        val intersection = Intersection(1.0, sphere)
        val intersectionData = ShadingPreComputation.of(ray, intersection)
        assertEquals(intersection.time, intersectionData.hitTime)
        assertEquals(intersection.shape, intersectionData.shape)
        assertEquals(point(0.0, 0.0, 1.0), intersectionData.intersectionPoint)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.eyeVector)
        assertEquals(vector(0.0, 0.0, -1.0), intersectionData.normalVector)
        assertEquals(true, intersectionData.intersectionOccursInside)
    }

    @Test()
    fun testCreationWithIllegalArguments() {
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                vector(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                false,
                vector(1.0, 1.0, 1.0)
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                point(1.0, 1.0, 1.0),
                point(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                vector(1.0, 1.0, 1.0),
                false,
                point(1.0, 1.0, 1.0)
            )
        }
    }

    @Test
    fun testHitShouldOffsetPoint() {
        val ray = Ray(point(0.0, 0.0, -5.0), vector(0.0, 0.0, 1.0))
        val sphere = Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, 1.0))
        val intersection = Intersection(5.0, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertTrue(computation.overPoint[2] < -EPSILON / 2)
        assertTrue(computation.intersectionPoint[2] > computation.overPoint[2])
    }

    @Test
    fun testPreComputeReflectionVector() {
        val ray = Ray(point(0.0, 1.0, -1.0), vector(0.0, -sqrt(2.0) / 2, sqrt(2.0) / 2))
        val plane = Plane()
        val intersection = Intersection(sqrt(2.0), plane)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertEquals(vector(0.0, sqrt(2.0) / 2, sqrt(2.0) / 2), computation.reflectVector)
    }

}
