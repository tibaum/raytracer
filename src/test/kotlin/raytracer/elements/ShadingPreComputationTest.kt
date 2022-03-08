package raytracer.elements

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ShadingPreComputationTest {

    @Test()
    fun testPrecomputingTheStateOfAnIntersectionOutside() {
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = Sphere()
        val intersection = Intersection(4.0, sphere)
        val intersectionData = ShadingPreComputation.of(ray, intersection)
        assertEquals(intersection.time, intersectionData.hitTime)
        assertEquals(intersection.shape, intersectionData.shape)
        assertEquals(Tuple.point(0.0, 0.0, -1.0), intersectionData.intersectionPoint)
        assertEquals(Tuple.vector(0.0, 0.0, -1.0), intersectionData.eyeVector)
        assertEquals(Tuple.vector(0.0, 0.0, -1.0), intersectionData.normalVector)
        assertEquals(false, intersectionData.intersectionOccursInside)
    }

    @Test()
    fun testPrecomputingTheStateOfAnIntersectionInside() {
        val ray = Ray(Tuple.point(0.0, 0.0, 0.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = Sphere()
        val intersection = Intersection(1.0, sphere)
        val intersectionData = ShadingPreComputation.of(ray, intersection)
        assertEquals(intersection.time, intersectionData.hitTime)
        assertEquals(intersection.shape, intersectionData.shape)
        assertEquals(Tuple.point(0.0, 0.0, 1.0), intersectionData.intersectionPoint)
        assertEquals(Tuple.vector(0.0, 0.0, -1.0), intersectionData.eyeVector)
        assertEquals(Tuple.vector(0.0, 0.0, -1.0), intersectionData.normalVector)
        assertEquals(true, intersectionData.intersectionOccursInside)
    }

    @Test()
    fun testCreationWithIllegalArguments() {
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                Tuple.vector(1.0, 1.0, 1.0),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                false
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                false
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                false
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            ShadingPreComputation(
                0.1,
                Sphere(),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.point(1.0, 1.0, 1.0),
                Tuple.vector(1.0, 1.0, 1.0),
                Tuple.point(1.0, 1.0, 1.0),
                false
            )
        }
    }

    @Test
    fun testHitShouldOffsetPoint() {
        val ray = Ray(Tuple.point(0.0, 0.0, -5.0), Tuple.vector(0.0, 0.0, 1.0))
        val sphere = Sphere(transformationMatrix = Matrix.translation(0.0, 0.0, 1.0))
        val intersection = Intersection(5.0, sphere)
        val computation = ShadingPreComputation.of(ray, intersection)
        assertTrue(computation.overPoint[2] < -EPSILON / 2)
        assertTrue(computation.intersectionPoint[2] > computation.overPoint[2])
    }

}
